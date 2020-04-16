import axios from 'axios'

const BASE_URL = `${process.env.REACT_APP_API_URL}/api/v1`;

export const login = (username, password) => axios.post(`${BASE_URL}/login`, {username, password});