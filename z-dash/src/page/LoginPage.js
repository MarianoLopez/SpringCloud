import React, {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux'
import {requestLogin} from "../store/action";
import {Header, LoginForm} from '../component'
import {Grid} from "@material-ui/core";
import Alert from '@material-ui/lab/Alert';
import Footer from "../component/Footer";
import {Redirect} from "react-router-dom";


export default (...props) => {
    const {authorities, error} = useSelector(state => ({
        authorities: state.loginResponse.user.claims.authorities,
        error: state.loginResponse.error
    }));

    const dispatch = useDispatch();
    const [loginForm, setLoginForm] = useState({
        username: '',
        password: ''
    });

    const onFormChange = (event) => {
        setLoginForm({
            ...loginForm,
            [event.target.id]: event.target.value
        })
    };
    const handleLogin = () => {
        dispatch(requestLogin({...loginForm}))
    };


    function renderError() {
        return error ?
            <Grid container justify="center" direction="row" alignItems="center" style={{margin: 5}}>
                <Alert severity="error">{error.title || error}</Alert>
            </Grid> : null;
    }

    if (authorities.includes("ROLE_ADMIN")) {
        return <Redirect to='/admin' />
    } else if (authorities.includes("ROLE_USER")) {
        return <Redirect to='/' />
    } else {
        return (
            <Grid container direction="row">
                <Header/>
                {renderError()}
                <LoginForm handleLogin={handleLogin} onFormChange={onFormChange}/>
                <Footer/>
            </Grid>
        );
    }
}