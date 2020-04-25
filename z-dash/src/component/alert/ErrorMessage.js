import {Grid} from "@material-ui/core";
import Alert from "@material-ui/lab/Alert";
import React from "react";

export default ({error}) => {
    return error ?
        <Grid container justify="center" direction="row" alignItems="center" style={{margin: 5}}>
            <Alert severity="error">{error.title || error}</Alert>
        </Grid> : null;
};