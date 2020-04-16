import React, {useEffect, useState} from 'react';
import {useDispatch, useSelector} from 'react-redux'
import {requestLogin, test} from "../store/action";
import {Header, LoginForm} from '../component'
import Grid from "@material-ui/core/Grid";
import Footer from "../component/Footer";


export default (...props) => {
    const {authorities, text} = useSelector(state => ({
        text: state.test,
        authorities: state.loginResponse.user.claims.authorities
    }));


    useEffect(() => {
        console.log('USE EFFECT', text)
        console.log('USE EFFECT authorities', authorities)
    });



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
        dispatch(test("sarasing"));
        dispatch(requestLogin({...loginForm}))
            /*.then(()=>{
                console.log("ajdhfkashdfkhasdjf")
                checkUserRoles()
            });*/
    };

  /*  const handleError = () => {
        if(loginResponse.error){
            return (
                <div>{loginResponse.error.title}</div>
            )
        }
    };*/

    function checkUserRoles() {
        console.log(authorities);
        const userRoles =authorities;
        console.log(userRoles)
        if (userRoles.includes("ROLE_USER")) {
            console.log("yes")
            props.history.push('/');
        }
    }

    return (
        <Grid container direction="row">
            <Header/>
            <LoginForm handleLogin={handleLogin} onFormChange={onFormChange}/>
            <Footer/>
        </Grid>
    );
}