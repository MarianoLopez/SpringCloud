import {Redirect, Route} from "react-router-dom";
import {LOGIN_PATH} from "../utils/path";
import ForbiddenPage from "./page/ForbiddenPage";
import React from "react";

export default ({component: Component, userRoles, needSome, ...rest}) => (
    <Route
        {...rest}
        render={props => {
            if (userRoles.length === 0) {
                return (props.location.pathname === LOGIN_PATH) ?
                    null : <Redirect to={{pathname: LOGIN_PATH, state: {from: props.location}}}/>;
            } else {
                return needSome.some(role => userRoles.includes(role)) ?
                    <Component {...props} /> : <ForbiddenPage/>;
            }
        }}
    />
);