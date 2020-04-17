import {REQUEST_LOGIN, LOGIN_ERROR} from "./action";

export const userReducer = (state, action) => {
    switch (action.type) {
        case REQUEST_LOGIN:
            return updateLoginResponse(state, 'user', action.payload);
        case LOGIN_ERROR:
            return updateLoginResponse(state, 'error', action.payload);
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