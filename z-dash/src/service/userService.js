import axiosInstance from './index'

let USER_URL = `/api/v1/user`;

if (process.env.NODE_ENV === 'production') {
    USER_URL = `/user-service${USER_URL}`
}

export const registration = (addUserRequest) => axiosInstance.post(USER_URL, addUserRequest);

export const findAll = (page, size, sort, search) => axiosInstance.get(USER_URL, {
    params: { page, size, sort, search }
});