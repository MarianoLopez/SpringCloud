import axiosInstance from './index'

let LOGIN_URL = `/user-service/api/v1/login`;

export const login = (username, password) => axiosInstance.post(LOGIN_URL, {username, password});