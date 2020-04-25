import React, {useState} from 'react';
import {useForm} from 'react-hook-form'
import {Button, CircularProgress, Grid, makeStyles, Paper, TextField} from '@material-ui/core';
import {blue} from "@material-ui/core/colors";
import {email, password, username} from "../../utils/validation";

const useStyles = makeStyles((theme) => ({
    wrapper: {
        margin: theme.spacing(1),
        position: 'relative',
    },
    buttonProgress: {
        color: blue,
        position: 'absolute',
        top: '50%',
        left: '50%',
        marginTop: -12,
        marginLeft: -12,
    },
    input: {
        margin: 5
    }
}));

export default ({onSubmit, isLoading, validations = {username, password, email}, disableAll = false}) => {
    const classes = useStyles();
    const {register, handleSubmit, errors} = useForm();
    const [userForm, setUserForm] = useState({
        username: '',
        email: '',
        password: '',
        rePassword: ''
    });

    validations = {
        ...validations,
        rePassword: {
            validate: value => value === userForm.password ? true : 'Passwords does not match',
            required: 'Re password is required'
        }
    };

    const onFormChange = (event) => {
        setUserForm({
            ...userForm,
            [event.target.id]: event.target.value
        })
    };

    return (
        <Grid container justify="center" direction="row" alignItems="center">
            <Paper style={{padding: 20}}>
                <form onChange={onFormChange} autoComplete="false" onSubmit={handleSubmit(onSubmit)}>
                    <Grid container spacing={6} alignItems="flex-end">
                        <Grid item>
                            <TextField id="username" label="Username" name="username"
                                       className={classes.input}
                                       inputRef={register(validations.username)}
                                       error={!!errors.username}
                                       helperText={errors.username ? errors.username.message : ''}
                                       size={"medium"} autoFocus disabled={disableAll}/>

                            <TextField id="email" label="email" name="email"
                                       className={classes.input}
                                       inputRef={register(validations.email)}
                                       error={!!errors.email}
                                       helperText={errors.email ? errors.email.message : ''}
                                       disabled={disableAll}
                            />
                        </Grid>
                    </Grid>
                    <Grid container spacing={6} alignItems="flex-end">
                        <Grid item>
                            <TextField id="password" name="password" label="Password" type="password"
                                       className={classes.input}
                                       inputRef={register(validations.password)}
                                       error={!!errors.password}
                                       helperText={errors.password ? errors.password.message : ''}
                                       disabled={disableAll}
                            />
                            <TextField id="rePassword" name="rePassword" label="rePassword" type="password"
                                       className={classes.input}
                                       inputRef={register(validations.rePassword)}
                                       error={!!errors.rePassword}
                                       helperText={errors.rePassword ? errors.rePassword.message : ''}
                                       disabled={disableAll}
                            />
                        </Grid>
                    </Grid>
                    <Grid container justify="center" style={{marginTop: '10px'}} className={classes.wrapper}>
                        <Button variant="outlined" color="primary"
                                disabled={isLoading || disableAll}
                                type="submit" style={{textTransform: "none"}}>
                            Submit
                        </Button>
                        {isLoading && <CircularProgress size={24} className={classes.buttonProgress}/>}
                    </Grid>
                </form>
            </Paper>
        </Grid>
    );
};
