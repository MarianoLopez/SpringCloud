import {login} from '../service/loginService'

export const LOGIN_SUCCESS = 'LOGIN_SUCCESS';
export const LOGIN_IN_PROGRESS = 'LOGIN_IN_PROGRESS';
export const LOGIN_ERROR = 'LOGIN_ERROR';
export const LOGOUT = 'LOGOUT';

export const logout = () => {
    return dispatch => {
        dispatch({
            type: LOGOUT,
            payload: null
        })
    }
};

export const requestLogin = ({username, password}) => {
    return dispatch => {
        dispatch({
            type: LOGIN_IN_PROGRESS,
            payload: true
        });
        return login(username, password)
            .then(res=> {
                dispatch({
                    type: LOGIN_SUCCESS,
                    payload: res.data
                })
            })
            .catch(err=> {
                dispatch({
                    type: LOGIN_ERROR,
                    payload: typeof err.response != 'undefined' ? err.response.data : err.message
                })
            });
    }
};