import React from "react";
import Grid from "@material-ui/core/Grid";
import './css/forbidden.css'
import {makeStyles} from "@material-ui/core/styles";
import indigo from "@material-ui/core/colors/indigo";
import Typography from "@material-ui/core/Typography";

const useStyles = makeStyles((theme) => ({
    main: {
        background: indigo[600],
        marginTop: -30,
        marginRight: -32,
        marginBottom: -100
    }
}));

export default () => {
    const classes = useStyles();
    return (

        <Grid item xl={12} lg={12} md={12} xs={12} className={classes.main}>
            <Grid container direction="row" justify="center" alignItems="center" style={{margin: 10}}>
                <div className="wrapper">
                    <div className="gandalf">
                        <div className="fireball"/>
                        <div className="skirt"/>
                        <div className="sleeves"/>
                        <div className="shoulders">
                            <div className="hand left"/>
                            <div className="hand right"/>
                        </div>
                        <div className="head">
                            <div className="hair"/>
                            <div className="beard"/>
                        </div>
                    </div>
                    <div className="message">
                        <Typography variant="h1" noWrap>403 - You Shall Not Pass</Typography>
                        <Typography variant="subtitle2">
                            Uh oh, Gandalf is blocking the way!<br/>Maybe you have a typo in the url? Or you meant to go
                            to a
                            different
                            location? Like...Hobbiton?
                            <br/>
                        </Typography>
                    </div>
                </div>
            </Grid>
        </Grid>
    );
}