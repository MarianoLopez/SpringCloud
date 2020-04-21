import React, {useState} from 'react';
import {ErrorMessage} from '../../component'
import {Grid} from "@material-ui/core";
import RegistrationForm from "../../component/RegistrationForm";
import {registration} from "../../service/UserService";


export default () => {
    const [registrationState, setRegistrationState] = useState({
        error: null,
        isLoading: false
    });

    const handleSubmit = (data, event) => {
        event.preventDefault();

        setRegistrationState({
            isLoading: true,
            error: null
        });
        registration(data)
            .then(res=> {
                setRegistrationState({
                    isLoading: false,
                    error: null
                });
                console.log(res.data);
            })
            .catch(err=>{
                let data = typeof err.response != 'undefined' ? err.response.data : err.message;
                console.log(data);
                setRegistrationState({
                    isLoading: false,
                    error: data
                })
            });
    };

    return (
        <Grid container direction="row">
            <ErrorMessage error={registrationState.error}/>
            <RegistrationForm  onSubmit={handleSubmit} isLoading={registrationState.isLoading}/>
        </Grid>
    );
}