import axiosInstance from './index'

const USER_URL = `user`;

export const registration = (addUserRequest) => axiosInstance.post(USER_URL, addUserRequest);