import {ALPHANUMERIC_5_20, EMAIL, ONLY_LETTERS_5_20} from "./regex";


const username = {
    required: 'Username is required',
    pattern: {
        value: ONLY_LETTERS_5_20,
        message: "Should be only letters between 5 and 20 characters"
    }
};
const password = {
    pattern: {
        value: ALPHANUMERIC_5_20,
        message: "Should be alphanumeric between 5 and 10 characters"
    },
    required: 'Password is required'
};
const email = {
    pattern: {
        value: EMAIL,
        message: "Should be a valid email address"
    },
    required: 'Email is required'
};

export {username, password, email}