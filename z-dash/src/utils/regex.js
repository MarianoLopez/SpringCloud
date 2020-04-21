//https://www.oreilly.com/library/view/regular-expressions-cookbook/9781449327453/ch04s09.html
const ALPHANUMERIC_5_20 = /^[a-zA-Z0-9]{5,20}$/;
const ONLY_LETTERS_5_20 = /^[A-Za-z]{5,20}$/;
const EMAIL = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

export {ALPHANUMERIC_5_20, ONLY_LETTERS_5_20, EMAIL}