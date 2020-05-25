import React from "react";
import {useSelector} from "react-redux";
import {BrowserRouter as Router, Switch} from "react-router-dom";
import {Grid, makeStyles} from "@material-ui/core";
import {AdminHomePage, LoginPage, RegistrationPage, ConfirmationPage, UserHomePage, UserListPage} from "./page/";
import {ADMIN_HOME, ADMIN_USER_LIST, CONFIRMATION_PATH, LOGIN_PATH, REGISTRATION_PATH, USER_HOME} from "../utils/path";
import {ROLE_ADMIN, ROLE_USER} from "../utils/role";
import {Footer, Header} from "../component";
import Routes from "./Routes";
import {AccountCircle, List} from "@material-ui/icons";

const createRouteItem = (path, roles, component, icon = null, text = null) => {
    return {path, roles, component, text, icon}
};

const routes = [
    createRouteItem(LOGIN_PATH, [], LoginPage),
    createRouteItem(REGISTRATION_PATH, [], RegistrationPage),
    createRouteItem(CONFIRMATION_PATH, [], ConfirmationPage),
    createRouteItem(USER_HOME, [ROLE_USER], UserHomePage, AccountCircle, 'Home'),
    createRouteItem(ADMIN_HOME, [ROLE_ADMIN], AdminHomePage, AccountCircle, 'Home'),
    createRouteItem(ADMIN_USER_LIST, [ROLE_ADMIN], UserListPage, List, 'User List')
];

const useStyles = makeStyles((theme) => ({
    toolbar: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-end',
        padding: theme.spacing(0, 1),
        // necessary for content to be below app bar
        ...theme.mixins.toolbar,
    },
    content: {
        flexGrow: 1,
        padding: theme.spacing(3),
        marginTop: 50
    },
}));

export default () => {
    const user = useSelector(state => state.loginResponse.user);
    const classes = useStyles();

    return (
        <Router>
            <Header user={user} routes={routes}/>
            <main className={classes.content}>
                <Grid className={classes.toolbar}>
                    <Switch>
                        <Routes configuration={routes} authorities={user.claims.authorities}/>
                    </Switch>
                </Grid>
            </main>
            <Footer/>
        </Router>
    );
}