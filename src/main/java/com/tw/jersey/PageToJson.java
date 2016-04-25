package com.tw.jersey;

import com.tw.core.Page;
import com.tw.core.Record;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static com.tw.util.Json.toJson;
import static java.util.stream.Collectors.toList;

@Provider
public class PageToJson implements MessageBodyWriter<Page<? extends Record>> {
    @Context
    javax.inject.Provider<Routes> routesProvider;

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (Page.class.isAssignableFrom(type)) {
            return true;
        }
        return false;
    }

    @Override
    public long getSize(Page<? extends Record> page, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }

    @Override
    public void writeTo(Page<? extends Record> page, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, mediaType.toString() + ";charset=UTF-8");
        Map<String, Object> pageJson = new HashMap<>();
        pageJson.put("self", page.getCurrent());
        pageJson.put("count", page.getTotalCount());
        pageJson.put("first", page.getFirst());
        pageJson.put("last", page.getLast());
        pageJson.put("prev", page.getPrev());
        pageJson.put("next", page.getNext());
        pageJson.put("items", page.getItems().stream().map((record) -> record.toRefJson(routesProvider.get())).collect(toList()));
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(entityStream)) {
            outputStreamWriter.write(toJson(pageJson));
        }
    }
}

