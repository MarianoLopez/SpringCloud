import {createStore, applyMiddleware} from 'redux';
import thunk from 'redux-thunk';
import logger from 'redux-logger';
import {saveAuthToken} from './middleware/axiosMiddleware';
import {userReducer} from './reducer';
import {setToken} from "../service";

const LOGIN_STORE = 'loginStore';

export const initialState = {
    isLoading: false,
    loginResponse: {
        error: null,
        user: {
            token: null,
            expires: null,
            claims: {
                authorities: []
            },
            subject: null,
            enabled: false
        }
    }
};
const loadState = (defaultState) => {
    let data = JSON.parse(localStorage.getItem(LOGIN_STORE)) || defaultState;
    setToken(data.loginResponse.user.token);
    return data;
};

const saveState = (state) => {
    try {
        localStorage.setItem(LOGIN_STORE, JSON.stringify(state))
    } catch (error) {
        console.error(error);
    }
};

const loginStore = createStore(userReducer, loadState(initialState), applyMiddleware(logger, thunk, saveAuthToken));
loginStore.subscribe(() => saveState(loginStore.getState()));

export default loginStore;