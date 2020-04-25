import {MetadataItem} from "../../../component/table/MetadaItem";
import Switch from "@material-ui/core/Switch";
import React from "react";
import {RolesChip} from "../../../component/surface/RolesChip";

export default  [
    MetadataItem("Name", "name"),
    MetadataItem("Email", "email"),
    MetadataItem("Roles", "roles", (field, id) => {
        return (
            <div>
                <RolesChip roles={field} id={id}/>
            </div>
        )
    }),
    MetadataItem("Created Date", "createdDate"),
    MetadataItem("State", "state", (field) => <Switch checked={field}  name="state" />)
];