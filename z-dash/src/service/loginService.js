import axiosInstance from './index'

let LOGIN_URL = `/api/v1/login`;

if (process.env.NODE_ENV === 'production') {
    LOGIN_URL = `/user-service${LOGIN_URL}`
}


export const login = (username, password) => axiosInstance.post(LOGIN_URL, {username, password});