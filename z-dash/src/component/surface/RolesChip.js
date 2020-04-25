import Chip from "@material-ui/core/Chip";
import {getKey} from "../../utils/reactUtils";
import React from "react";

export const RolesChip = ({roles, id = -1}) => {
    return (
        <>{
            roles.map(role =>
                <Chip
                    variant="outlined" color="primary" size="small"
                    key={getKey(role, id)}
                    label={role.replace('ROLE_', '').toLocaleLowerCase()}
                    style={{margin: 2}}
                />)
        }</>
    );
};