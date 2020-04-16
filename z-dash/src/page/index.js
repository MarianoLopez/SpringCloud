import React from "react";
import {BrowserRouter as Router, Redirect, Route, Switch} from "react-router-dom";
import LoginPage from "./LoginPage";
import UserHomePage from "./UserHomePage";
import ForbiddenPage from "./ForbiddenPage";
import {useSelector} from "react-redux";

const LOGIN_PATH = '/login';
const USER_HOME = '/';

const routes = [
    {path: LOGIN_PATH, roles:[], component:LoginPage},
    {path: USER_HOME, roles:["ROLE_USER"], component:UserHomePage}
];

export default (...props) => {
    return (
        <Router>
            <Switch>
               <Routes/>
            </Switch>
        </Router>
    );
}

const Routes = () => {
    const loginResponse = useSelector(state => state.loginResponse);
    const userRoles = loginResponse.user.claims.authorities;

    return routes.map((route,i) => {
        if (route.roles.length > 0){
            return <PrivateRoute key={i} path={route.path} exact
                                 component={route.component} needSome={route.roles}
                                 userRoles={userRoles} />
        } else{
            return route.path === LOGIN_PATH && userRoles.length > 0 ?
                null : <Route key={i} path={route.path} exact
                              component={route.component} />;
        }
    });
};

const PrivateRoute = ({ component: Component, userRoles, needSome, ...rest }) => (
    <Route
        {...rest}
        render={props =>{
            if(userRoles.length === 0){
                return (props.location.pathname === LOGIN_PATH) ?
                    null : <Redirect to={{pathname: LOGIN_PATH, state: { from: props.location }}} />;
            }else{
                return needSome.some(role => userRoles.includes(role)) ?
                    <Component {...props} /> : <ForbiddenPage/>;
            }
        }}
    />
);