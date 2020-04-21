import DrawerList from "./DrawerList";
import React, {useEffect} from "react";
import {AccountCircle} from "@material-ui/icons";

const userItems = [
    {
        text: 'Home',
        icon: AccountCircle,
        path: "/"
    }
];
const userKeyPrefix = "userDrawerList";
export default ({selectedItemId, onSelectItemId}) => {
    const setDefaultKey = () => {
        if (!selectedItemId) {
            onSelectItemId(`${userKeyPrefix}-0`);
        }
    };

    useEffect(() => {
        setDefaultKey();
    });

    return (
        <DrawerList items={userItems} keyPrefix={userKeyPrefix}
                    selectedItemId={selectedItemId} onSelectItemId={onSelectItemId}/>
    );
}