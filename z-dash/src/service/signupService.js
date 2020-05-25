import axiosInstance from './index'

let SIGN_UP_URL = `/user-service/api/v1/sign-up`;

export const registration = (addUserRequest) => axiosInstance.post(SIGN_UP_URL, addUserRequest);

export const confirm = (confirmationToken) => axiosInstance.put(SIGN_UP_URL, null,{
    params: {confirmationToken}
});