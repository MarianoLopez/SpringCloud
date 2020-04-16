import React from 'react';
import {useForm} from 'react-hook-form'
import {Button, Grid, Paper, TextField} from '@material-ui/core';
import {Face, Fingerprint} from '@material-ui/icons'
import {ALPHANUMERIC_5_20, ONLY_LETTERS_5_20} from '../utils/regex'

const defaultValidations = {
    username: {
        required: 'Username is required',
        pattern: {
            value: ONLY_LETTERS_5_20,
            message: "Should be only letters between 5 and 20 characters"
        }
    },
    password: {
        pattern: {
            value: ALPHANUMERIC_5_20,
            message: "Should be alphanumeric between 5 and 10 characters"
        },
        required: 'Password is required'
    }
};

export default ({ onFormChange, handleLogin, validations = defaultValidations }) => {
    const {register, handleSubmit, errors} = useForm();

    return (
        <Grid container justify="center" direction="row" alignItems="center" style={{margin: 10}} className="loginForm">
            <Paper style={{padding: 20}}>
                <form onChange={onFormChange} autoComplete="false" onSubmit={handleSubmit(handleLogin)}>
                    <Grid container spacing={6} alignItems="flex-end">
                        <Grid item>
                            <Face/>
                        </Grid>
                        <Grid item>
                            <TextField id="username" label="Username" name="username"
                                       inputRef={register(validations.username)}
                                       error={!!errors.username}
                                       helperText={errors.username ? errors.username.message : ''}
                                       fullWidth autoFocus/>
                        </Grid>
                    </Grid>
                    <Grid container spacing={6} alignItems="flex-end">
                        <Grid item>
                            <Fingerprint/>
                        </Grid>
                        <Grid item md sm xs>
                            <TextField id="password" name="password" label="Password" type="password"
                                       inputRef={register(validations.password)}
                                       error={!!errors.password}
                                       helperText={errors.password ? errors.password.message : ''}
                                       fullWidth/>
                        </Grid>
                    </Grid>
                    <Grid container justify="center" style={{marginTop: '10px'}}>
                        <Button variant="outlined" color="primary" type="submit" style={{textTransform: "none"}}>
                            Login
                        </Button>
                    </Grid>
                </form>
            </Paper>
        </Grid>
    );
};
