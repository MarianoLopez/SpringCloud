import {Redirect} from "react-router-dom";
import React from "react";
import {useSelector} from "react-redux";
import {ROLE_ADMIN, ROLE_USER} from "../../utils/role";
import {ADMIN_HOME, USER_HOME} from "../../utils/path";

export default ({children}) => {
    const authorities = useSelector(state => state.loginResponse.user.claims.authorities);

    if (authorities.includes(ROLE_ADMIN)) {
        return <Redirect to={ADMIN_HOME}/>
    } else if (authorities.includes(ROLE_USER)) {
        return <Redirect to={USER_HOME}/>
    } else {
        return children;
    }
};