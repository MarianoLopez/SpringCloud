import React, {useState} from 'react';
import {Button, CircularProgress, Grid, makeStyles, Paper, TextField} from '@material-ui/core';
import {blue} from "@material-ui/core/colors";

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
    }
}));

export default ({token, handleSubmit, isLoading, disableAll}) => {
    const [formState, setFormState] = useState({
        token: token
    });

    const onFormChange = (event) => {
        setFormState({
            ...formState,
            [event.target.id]: event.target.value
        })
    };


    const classes = useStyles();
    return (
        <Grid container justify="center" direction="row" alignItems="center" style={{margin: 10}}>
            <Grid item xs={6} xl={6} md={6} sm={6} lg={6}>
                <Paper style={{padding: 20}}>
                    <form autoComplete="false" onSubmit={event =>  handleSubmit(event, formState)} onChange={onFormChange}>
                        <Grid item>
                            <TextField id="token" label="token" name="token" value={formState.token} fullWidth autoFocus disabled={disableAll}/>
                        </Grid>
                        <Grid container justify="center" style={{marginTop: '10px'}} className={classes.wrapper}>
                            <Button variant="outlined" color="primary"
                                    disabled={isLoading || disableAll}
                                    type="submit" style={{textTransform: "none"}}>
                                Confirm
                            </Button>
                            {isLoading && <CircularProgress size={24} className={classes.buttonProgress}/>}
                        </Grid>
                    </form>
                </Paper>
            </Grid>
        </Grid>
    );
};
