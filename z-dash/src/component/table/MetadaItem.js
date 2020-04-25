import React from "react";
import {getKey} from "../../utils/reactUtils";
import Skeleton from "@material-ui/lab/Skeleton";

const defaultRender = (field, id) => {
    return (
        <div className={getKey(field, id)}>{field.toString()}</div>
    );
};

const defaultSkeleton = () => <Skeleton variant="text" animation="wave"/>;

export const MetadataItem = (name, field, render = defaultRender, skeleton = defaultSkeleton) => {
    return { name, field, render, skeleton}
};

