import React, {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux'
import {requestLogin} from "../../store/action";
import {ErrorMessage, LoginForm} from '../../component'
import {Grid} from "@material-ui/core";
import LoginController from "../controller/LoginController";


export default () => {
    const {error, isLoading} = useSelector(state => ({
        error: state.loginResponse.error,
        isLoading: state.isLoading
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
                <ErrorMessage error={error}/>
                <LoginForm handleLogin={handleLogin} onFormChange={onFormChange} isLoading={isLoading}/>
            </Grid>
        </LoginController>
    );
}