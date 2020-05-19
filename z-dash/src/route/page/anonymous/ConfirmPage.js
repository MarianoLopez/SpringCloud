import React, {useState} from 'react';
import {ErrorMessage} from '../../../component'
import {Grid} from "@material-ui/core";
import {confirm} from "../../../service/signupService";
import SuccessMessage from "../../../component/alert/SuccessMessage";
import Typography from "@material-ui/core/Typography";
import {Link} from "react-router-dom";
import {LOGIN_PATH} from "../../../utils/path";
import LoginController from "../../controller/LoginController";
import ConfirmForm from "../../../component/form/ConfirmForm";

export default () => {
    const [confirmState, setConfirmState] = useState({
        error: null,
        success: false,
        isLoading: false
    });

    const enableLoading = () => {
        setConfirmState({
            success: false,
            isLoading: true,
            error: null
        });
    };
    const setSuccess = () => {
        setConfirmState({
            isLoading: false,
            error: null,
            success: true
        });
    };
    const setError = (error) => {
        setConfirmState({
            success: false,
            isLoading: false,
            error: error
        })
    };

    const handleSubmit = (event, data) => {
        event.preventDefault();
        enableLoading();
        confirm(data.token)
            .then(res => {
                setSuccess(res.data);
            })
            .catch(err => {
                let data = typeof err.response != 'undefined' ? err.response.data : err.message;
                setError(data)
            });
    };

    function renderSuccessMessage() {
        return confirmState.success ?
            (
                <SuccessMessage>
                    <Typography>
                        User confirmed successfully.<br/>
                        Now Go to the <Link to={LOGIN_PATH}>Login</Link> page.
                    </Typography>
                </SuccessMessage>
            )
            : null;
    }


    return (
        <LoginController>
            <Grid container direction="row">
                {renderSuccessMessage()}
                <ErrorMessage error={confirmState.error}/>
                <ConfirmForm handleSubmit={handleSubmit} isLoading={confirmState.isLoading} disableAll={confirmState.success}/>
            </Grid>
        </LoginController>
    );
}