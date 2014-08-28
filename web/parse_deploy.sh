pwd

## cleaning
rm -rf parse/public/*

## copy
cp -Rf dist/* parse/public

cd parse
parse deploy one-liners
