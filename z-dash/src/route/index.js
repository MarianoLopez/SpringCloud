import React from "react";
import {useSelector} from "react-redux";
import {BrowserRouter as Router, Switch} from "react-router-dom";
import {Grid, makeStyles} from "@material-ui/core";
import {AdminHomePage, LoginPage, RegistrationPage, UserHomePage} from "./page/";
import {ADMIN_HOME, LOGIN_PATH, REGISTRATION_PATH, USER_HOME} from "../utils/path";
import {ROLE_ADMIN, ROLE_USER} from "../utils/role";
import {Footer, Header} from "../component";
import Routes from "./Routes";

const routes = [
    {path: LOGIN_PATH, roles: [], component: LoginPage},
    {path: REGISTRATION_PATH, roles: [], component: RegistrationPage},
    {path: USER_HOME, roles: [ROLE_USER], component: UserHomePage},
    {path: ADMIN_HOME, roles: [ROLE_ADMIN], component: AdminHomePage}
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
            <Header user={user}/>
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