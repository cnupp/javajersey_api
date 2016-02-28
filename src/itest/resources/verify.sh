#!/bin/bash

REPOSITORY_PATH=/codebase
GIT_PATH=/gitbare
CURRENT_DIR=`pwd`

puts_red() {
    echo $'\033[0;31m'"      $@" $'\033[0m'
}

puts_red_f() {
  while read data; do
    echo $'\033[0;31m'"      $data" $'\033[0m'
  done
}

puts_green() {
  echo $'\033[0;32m'"      $@" $'\033[0m'
}

puts_step() {
  echo $'\033[0;34m'" -----> $@" $'\033[0m'
}

if [ -f "$REPOSITORY_PATH/manifest.json" ]; then
    cd GIT_PATH
    git --no-pager log --reverse --pretty=format:%at &>/dev/null
    if [ "$?" != "0" ]; then
        first_commit=$(stat -c%Y config)
    else
        first_commit=$(git --no-pager log --reverse --pretty=format:%at |head -n1)
    fi

    cd $REPOSITORY_PATH
    evaluation_uri=$(cat manifest.json| jq -r '.evaluation_uri')

    echo "$evaluation_uri"
    if [ -z "$evaluation_uri" ] ; then
        puts_red "missing manifest.json"
        exit 1
    fi
    entry_point=$(echo $evaluation_uri | awk -F/ '{print $3}')
    if [ -z "$entry_point" ] ; then
        puts_red "bad format of manifest.json"
        exit 1
    fi
    echo "entrypoint=${entry_point}" > $CURRENT_DIR/src/itest/resources/meta.properties
    echo "evaluation_uri=${evaluation_uri}" >> $CURRENT_DIR/src/itest/resources/meta.properties
    echo "first_commit=${first_commit}" >> $CURRENT_DIR/src/itest/resources/meta.properties
fi
