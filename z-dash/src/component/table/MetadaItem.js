import React from "react";
import {getKey} from "../../utils/reactUtils";

const defaultRender = (field, id) => {
    return (
        <div className={getKey(field, id)}>{field.toString()}</div>
    );
};

export const MetadataItem = (name, field, render = defaultRender) => {
    return { name, field, render }
};

