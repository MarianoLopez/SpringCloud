import {Grid} from "@material-ui/core";
import Alert from "@material-ui/lab/Alert";
import React from "react";

export default ({children}) => {
    return (
        <Grid container justify="center" direction="row" alignItems="center" style={{margin: 5}}>
            <Alert severity="success">{children}</Alert>
        </Grid>
    );
};