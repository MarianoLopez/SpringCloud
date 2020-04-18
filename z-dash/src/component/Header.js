import React from "react";
import conditionalClasses from 'clsx';
import {makeStyles, useTheme} from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import CssBaseline from '@material-ui/core/CssBaseline';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import Grid from "@material-ui/core/Grid";
import Drawer from "@material-ui/core/Drawer";
import ChevronRightIcon from "@material-ui/icons/ChevronRight";
import ChevronLeftIcon from "@material-ui/icons/ChevronLeft";
import Divider from "@material-ui/core/Divider";
import {ROLE_ADMIN, ROLE_USER} from "../utils/role";
import UserDrawerList from "./drawer/UserDrawerList";
import AdminDrawerList from "./drawer/AdminDrawerList";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import {ExitToApp} from "@material-ui/icons";

const drawerWidth = 240;

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
    },
    appBar: {
        zIndex: theme.zIndex.drawer + 1,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
    },
    appBarShift: {
        marginLeft: drawerWidth,
        width: `calc(100% - ${drawerWidth}px)`,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    menuButton: {
        marginRight: 36,
    },
    hide: {
        display: 'none',
    },
    drawer: {
        width: drawerWidth,
        flexShrink: 0,
        whiteSpace: 'nowrap',
    },
    drawerOpen: {
        width: drawerWidth,
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    drawerClose: {
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
        overflowX: 'hidden',
        width: theme.spacing(7) + 1,
        [theme.breakpoints.up('sm')]: {
            width: theme.spacing(9) + 1,
        },
    },
    toolbar: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-end',
        padding: theme.spacing(0, 1),
        // necessary for content to be below app bar
        ...theme.mixins.toolbar,
    },
    content: {
        flexGrow: 1,
        padding: theme.spacing(3),
        marginTop: 50
    },
}));


export default ({user}) => {
    const classes = useStyles();
    const theme = useTheme();
    const [open, setOpen] = React.useState(false);

    const handleDrawerOpen = () => {
        setOpen(true);
    };

    const handleDrawerClose = () => {
        setOpen(false);
    };

    const DashDrawer = () => {
        const RenderAdminList = () => {
            if (user.claims.authorities.includes(ROLE_ADMIN)) {
                return (
                    <>
                        <AdminDrawerList/>
                        <Divider/>
                    </>
                )
            } else {
                return null;
            }
        };

        const RenderUserList = () => {
            if (user.claims.authorities.includes(ROLE_USER)) {
                return (
                    <>
                        <UserDrawerList/>
                        <Divider/>
                    </>
                )
            } else {
                return null;
            }
        };

        const RenderLogout = () => {
            return (
                <ListItem button key="logout">
                    <ListItemIcon>
                        <ExitToApp/>
                    </ListItemIcon>
                    <ListItemText primary="logout"/>
                </ListItem>
            );
        };

        const RenderList = () => {
            return (
                <>
                    <RenderAdminList/>
                    <RenderUserList/>
                    <RenderLogout/>
                </>
            )
        };

        if (user.token) {
            return (
                <Drawer
                    variant="permanent"
                    className={conditionalClasses(classes.drawer, {
                        [classes.drawerOpen]: open,
                        [classes.drawerClose]: !open,
                    })}
                    classes={{
                        paper: conditionalClasses({
                            [classes.drawerOpen]: open,
                            [classes.drawerClose]: !open,
                        }),
                    }}>
                    <div className={classes.toolbar}>
                        <IconButton onClick={handleDrawerClose}>
                            {theme.direction === 'rtl' ? <ChevronRightIcon/> : <ChevronLeftIcon/>}
                        </IconButton>
                    </div>
                    <Divider/>
                    <RenderList/>
                    <Divider/>
                </Drawer>
            );
        } else {
            return null;
        }
    };

    const DrawerIcon = () => {
        if (user.token) {
            return (
                <IconButton
                    color="inherit"
                    aria-label="open drawer"
                    onClick={handleDrawerOpen}
                    edge="start"
                    className={conditionalClasses(classes.menuButton, {
                        [classes.hide]: open,
                    })}>
                    <MenuIcon/>
                </IconButton>
            )
        } else {
            return null;
        }
    };

    return (
        <Grid className={classes.root}>
            <CssBaseline/>
            <AppBar position="fixed"
                    className={conditionalClasses(classes.appBar, {
                        [classes.appBarShift]: open,
                    })}>
                <Toolbar>
                    <DrawerIcon/>
                    <Typography variant="h6" noWrap>Z-Dash</Typography>
                </Toolbar>
            </AppBar>
            <DashDrawer/>
        </Grid>
    );
}