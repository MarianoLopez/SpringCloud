import {LOGIN_REQUEST, LOGIN_ERROR, LOGIN_IN_PROGRESS, LOGOUT} from "./action";
import {initialState} from "./index";

export const userReducer = (state, action) => {
    switch (action.type) {
        case LOGIN_REQUEST:
            return updateLoginResponse(state, 'user', action.payload);
        case LOGIN_ERROR:
            return updateLoginResponse(state, 'error', action.payload);
        case LOGIN_IN_PROGRESS:
            return {
                ...state,
                isLoading: true,
                loginResponse: {
                    ...state.loginResponse,
                    error: null
                }
            };
        case LOGOUT:
            return initialState;
        default:
            return state;
    }
};

const updateLoginResponse = (state, key, value) => {
    return {
        ...state,
        isLoading: false,
        loginResponse: {
            ...state.loginResponse,
            error: null,
            [key]: value
        }
    }
};