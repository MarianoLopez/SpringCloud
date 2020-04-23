import {LOGIN_SUCCESS, LOGOUT} from "../action";
import {setToken} from "../../service";

export const saveAuthToken = store => next => action => {
    if (action.type === LOGIN_SUCCESS) {
        setToken(action.payload.token);
    } else if (action.type === LOGOUT) {
        setToken(null);
    }
    return next(action);
};