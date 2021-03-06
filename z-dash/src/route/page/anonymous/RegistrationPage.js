import React, {useState} from 'react';
import {ErrorMessage} from '../../../component'
import {Grid} from "@material-ui/core";
import RegistrationForm from "../../../component/form/RegistrationForm";
import {registration} from "../../../service/signupService";
import SuccessMessage from "../../../component/alert/SuccessMessage";
import Typography from "@material-ui/core/Typography";
import {Link} from "react-router-dom";
import {CONFIRMATION_PATH} from "../../../utils/path";
import LoginController from "../../controller/LoginController";


export default () => {
    const [registrationState, setRegistrationState] = useState({
        error: null,
        success: false,
        isLoading: false
    });

    const enableLoading = () => {
        setRegistrationState({
            success: false,
            isLoading: true,
            error: null
        });
    };
    const setSuccess = () => {
        setRegistrationState({
            isLoading: false,
            error: null,
            success: true
        });
    };
    const setError = (error) => {
        setRegistrationState({
            success: false,
            isLoading: false,
            error: error
        })
    };

    const handleSubmit = (data, event) => {
        event.preventDefault();
        enableLoading();
        registration(data)
            .then(res => {
                setSuccess(res.data);
            })
            .catch(err => {
                let data = typeof err.response != 'undefined' ? err.response.data : err.message;
                setError(data)
            });
    };

    function renderSuccessMessage() {
        return registrationState.success ?
            (
                <SuccessMessage>
                    <Typography>
                        User created successfully.<br/>
                        Check your email and go to the <Link to={CONFIRMATION_PATH}>Confirm</Link> page.
                    </Typography>
                </SuccessMessage>
            )
            : null;
    }

    return (
        <LoginController>
            <Grid container direction="row">
                {renderSuccessMessage()}
                <ErrorMessage error={registrationState.error}/>
                <RegistrationForm onSubmit={handleSubmit}
                                  disableAll={registrationState.success}
                                  isLoading={registrationState.isLoading}/>
            </Grid>
        </LoginController>
    );
}