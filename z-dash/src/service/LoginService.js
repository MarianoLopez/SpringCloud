import axiosInstance from './index'

const LOGIN_URL = `login`;

export const login = (username, password) => axiosInstance.post(LOGIN_URL, {username, password});