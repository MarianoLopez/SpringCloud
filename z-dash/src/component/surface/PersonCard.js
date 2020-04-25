import {Grid} from "@material-ui/core";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import Avatar from "@material-ui/core/Avatar";
import Typography from "@material-ui/core/Typography";
import CardActions from "@material-ui/core/CardActions";
import React from "react";
import makeStyles from "@material-ui/core/styles/makeStyles";
import {RolesChip} from "./RolesChip";

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
        '& > *': {
            margin: theme.spacing(1),
        },
    },
}));


export default ({username, authorities}) => {
    const classes = useStyles();
    return (
        <Grid container direction="row" justify="center" alignItems="center" style={{margin: 10}}>
            <Grid xl={6} md={6} xs={8} item>
                <Card>
                    <CardContent>
                        <Grid className={classes.root}>
                            <Avatar alt="user" src="/user_avatar.png"/>
                            <Typography variant="h5" component="h2">{username}</Typography>
                        </Grid>
                        <Grid className={classes.root}>
                            <RolesChip roles={authorities} id={username}/> {/*TODO replace with id*/}
                        </Grid>
                        <Typography color="textSecondary">
                            Welcome back!
                        </Typography>
                        <Typography variant="body2" component="p">
                            .......................
                        </Typography>
                    </CardContent>
                    <CardActions>
                    </CardActions>
                </Card>
            </Grid>
        </Grid>
    );
}