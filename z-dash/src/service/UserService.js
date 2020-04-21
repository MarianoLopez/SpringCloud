import axios from "axios";
import {BASE_URL} from "./index";

const USER_URL = `${BASE_URL}/user`;

export const registration = (addUserRequest) => axios.post(USER_URL, addUserRequest);