import axios from 'axios'
import {BASE_URL} from "./index";

const LOGIN_URL = `${BASE_URL}/login`;

export const login = (username, password) => axios.post(LOGIN_URL, {username, password});