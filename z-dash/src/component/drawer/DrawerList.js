import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import List from "@material-ui/core/List";
import React from "react";
import {Link} from "react-router-dom";

export default ({items, keyPrefix}) => {
    const RenderIcon = ({icon: Component}) => {
        return Component ?
            <ListItemIcon>
                <Component/>
            </ListItemIcon>
            : null
    };

    return (
        <List>
            {items.map((item, index) => (
                <ListItem button key={`${keyPrefix}-${index}`} component={Link} to={item.path}>
                        <RenderIcon icon={item.icon}/>
                        <ListItemText primary={item.text}/>
                </ListItem>
            ))}
        </List>
    );
}