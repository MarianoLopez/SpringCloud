import React, {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux'
import {requestLogin} from "../store/action";
import {ErrorMessage, Header, LoginForm} from '../component'
import {Grid} from "@material-ui/core";
import Footer from "../component/Footer";
import LoginController from "./controller/LoginController";


export default () => {
    const {error} = useSelector(state => ({
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

    return (
        <LoginController>
            <Grid container direction="row">
                <Header/>
                <ErrorMessage error={error}/>
                <LoginForm handleLogin={handleLogin} onFormChange={onFormChange}/>
                <Footer/>
            </Grid>
        </LoginController>
    );
}