#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_e5c65c774293_key -iv $encrypted_e5c65c774293_iv -in deployment/signingkey.asc.enc -out deployment/signingkey.asc -d
    gpg --fast-import deployment/signingkey.asc
fi
