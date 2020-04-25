import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import List from "@material-ui/core/List";
import React, {useEffect} from "react";
import {Link} from "react-router-dom";

export default ({items, keyPrefix = 'drawerItem', selectedItemId, onSelectItemId}) => {
    const setDefaultKey = () => {
        if (!selectedItemId) {
            onSelectItemId(`${keyPrefix}-0`);
        }
    };

    useEffect(() => {
        setDefaultKey();
    });


    const RenderIcon = ({icon: Component}) => {
        return Component ?
            <ListItemIcon>
                <Component/>
            </ListItemIcon>
            : null
    };
    return (
        <List>
            {items.map((item, index) => {
            let id = `${keyPrefix}-${index}`;
            return (
                <ListItem button key={id} component={Link} to={item.path}
                          selected={id === selectedItemId}
                          onClick={()=> onSelectItemId(id)}>
                        <RenderIcon icon={item.icon}/>
                        <ListItemText primary={item.text}/>
                </ListItem>
            )
            })}
        </List>
    );
}