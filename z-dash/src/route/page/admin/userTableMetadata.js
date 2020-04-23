import {MetadataItem} from "../../../component/table/MetadaItem";
import Chip from "@material-ui/core/Chip";
import {getKey} from "../../../utils/reactUtils";
import {capitalizeFirstLetter} from "../../../utils/stringUtils";
import Switch from "@material-ui/core/Switch";
import React from "react";

export default  [
    MetadataItem("Name", "name"),
    MetadataItem("Email", "email"),
    MetadataItem("Roles", "roles", (field, id) => {
        return (
            <div>{
                field.map(role =>
                    <Chip
                        variant="outlined" color="primary" size="small"
                        key={getKey(role, id)}
                        label={capitalizeFirstLetter(role.replace('ROLE_').toLocaleLowerCase())}
                        style={{margin: 2}}
                    />)
            }</div>
        )
    }),
    MetadataItem("CreatedDate", "createdDate"),
    MetadataItem("State", "state", (field) => <Switch checked={field}  name="state" />)
];