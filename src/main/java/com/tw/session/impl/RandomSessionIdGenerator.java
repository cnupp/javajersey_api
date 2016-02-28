package com.tw.session.impl;

import com.tw.session.core.SessionIdGenerator;

import java.util.UUID;

public class RandomSessionIdGenerator implements SessionIdGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
