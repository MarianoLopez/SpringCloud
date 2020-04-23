import axiosInstance from './index'

const USER_URL = `user`;

export const registration = (addUserRequest) => axiosInstance.post(USER_URL, addUserRequest);

export const findAll = (page, size, sort) => axiosInstance.get(USER_URL, {
    params: { page, size, sort }
});