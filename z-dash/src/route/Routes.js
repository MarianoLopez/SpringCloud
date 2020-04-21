import React from "react";
import {Route} from "react-router-dom";
import PrivateRoute from "./PrivateRoute";

export default ({configuration, authorities}) => {
    return configuration.map((route, i) => {
        if (route.roles.length > 0) {
            return <PrivateRoute key={i} path={route.path} exact
                                 component={route.component} needSome={route.roles}
                                 userRoles={authorities}/>
        } else {
            return <Route key={i} path={route.path} exact
                          component={route.component}/>;
        }
    });
};