import {REQUEST_LOGIN, LOGIN_ERROR, TEST} from "./action";

const initialState = {
    test: 'test',

    loginResponse: {
        error: null,
        user: {
            "token": null,
            "expires": null,
            "claims": {
                "authorities": []
            },
            "subject": null,
            "enabled": false
        }
    }
};

export const userReducer = (state= initialState, action) => {
    switch (action.type) {
        case REQUEST_LOGIN:
            return updateLoginResponse(state, 'user', action.payload);
        case LOGIN_ERROR:
            return updateLoginResponse(state, 'error', action.payload);
        case TEST:
            return {
                ...state,
                test: action.payload
            };
        default:
            return state;
    }
};

const updateLoginResponse = (state, key, value) => {
    return {
        ...state,
        loginResponse: {
            ...state.loginResponse,
            [key]: value
        }
    }
};