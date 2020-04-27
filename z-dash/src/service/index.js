import axios from 'axios';
export const BASE_URL = process.env.REACT_APP_API_URL;

const instance = axios.create({
    baseURL: BASE_URL,
    withCredentials: true
});

export function setToken(token) {
    if (token != null) {
        instance.defaults.headers.common['Authorization'] = token;
    } else {
        delete instance.defaults.headers.common['Authorization']
    }
}

export default instance;