import DrawerList from "./DrawerList";
import React from "react";
import {AccountCircle} from "@material-ui/icons";

const userItems = [
    {
        text: 'Home',
        icon: AccountCircle,
        path: "/"
    }
];

export default () => {
    return (
        <DrawerList items={userItems} keyPrefix="userDrawerList"/>
    );
}