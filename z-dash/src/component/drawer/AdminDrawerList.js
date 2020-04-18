import DrawerList from "./DrawerList";
import React from "react";
import {AccountCircle} from "@material-ui/icons";

const userItems = [
    {
        text: 'Admin Home',
        icon: AccountCircle,
        path: "/admin"
    }
];

export default () => {
    return (
        <DrawerList items={userItems} keyPrefix="userDrawerList"/>
    );
}