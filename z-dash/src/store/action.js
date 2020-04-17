import {login} from '../service/loginService'

export const REQUEST_LOGIN = 'REQUEST_LOGIN';
export const LOGIN_ERROR = 'LOGIN_ERROR';

export const requestLogin = ({username, password}) => {
    return dispatch => {
        return login(username, password)
            .then(res=> {
                dispatch({
                    type: REQUEST_LOGIN,
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