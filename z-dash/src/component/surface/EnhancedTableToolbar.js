import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import React from "react";

export const EnhancedTableToolbar = ({title}) => {
    return (
        <Toolbar>
            <Typography variant="h6" id="tableTitle" component="div">
                {title}
            </Typography>
        </Toolbar>
    );
};