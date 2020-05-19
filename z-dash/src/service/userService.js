import axiosInstance from './index'

let USER_URL = `/user-service/api/v1/user`;

export const findAll = (page, size, sort, search) => axiosInstance.get(USER_URL, {
    params: { page, size, sort, search }
});