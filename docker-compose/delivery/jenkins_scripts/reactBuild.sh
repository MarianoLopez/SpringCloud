#!/bin/sh

cat ./package.json
npm ci --silent
npm run build