import DrawerList from "./DrawerList";
import React, {useEffect} from "react";
import {AccountCircle} from "@material-ui/icons";

const userItems = [
    {
        text: 'Admin Home',
        icon: AccountCircle,
        path: "/admin"
    }
];
const adminKeyPrefix = "adminDrawerList";
export default ({onSelectItemId, selectedItemId}) => {
    const setDefaultKey = () => {
        if (!selectedItemId) {
            onSelectItemId(`${adminKeyPrefix}-0`);
        }
    };

    useEffect(() => {
        setDefaultKey();
    });

    return (
        <DrawerList items={userItems} keyPrefix={adminKeyPrefix}
                    onSelectItemId={onSelectItemId} selectedItemId={selectedItemId}/>
    );
}