import {login} from '../service/loginService'

export const LOGIN_REQUEST = 'LOGIN_REQUEST';
export const LOGIN_IN_PROGRESS = 'LOGIN_IN_PROGRESS';
export const LOGIN_ERROR = 'LOGIN_ERROR';

export const requestLogin = ({username, password}) => {
    return dispatch => {
        dispatch({
            type: LOGIN_IN_PROGRESS,
            payload: true
        });
        return login(username, password)
            .then(res=> {
                dispatch({
                    type: LOGIN_REQUEST,
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