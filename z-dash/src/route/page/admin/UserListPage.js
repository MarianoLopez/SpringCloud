import {Grid} from "@material-ui/core";
import React, {useEffect, useState} from "react";
import {findAll} from "../../../service/UserService";
import {ZTable} from "../../../component/table/ZTable";
import metadata from "./userTableMetadata";
import {ErrorMessage} from "../../../component";

const defaultPage = {
    number: 0,
    size: 10,
    sort: 'name,asc'
};

export default () => {
    const [page, setPage] = useState(defaultPage);

    const [userPageableList, setUserPageableList] = useState({
        content: [],
        error: null
    });

    const fetchData = () => {
        findAll(page.number, page.size, page.sort)
            .then(res => {
                setUserPageableList({
                    error: null,
                    content: res.data.content
                })
            })
            .catch(err => {
                setUserPageableList({
                    error: typeof err.response != 'undefined' ? err.response.data : err.message,
                    content: []
                })
            })
    };

    useEffect(fetchData, [page]);

    const handleFetchRequest = (sort) => {
        setPage({
            ...page,
            sort: `${sort.by},${sort.direction}`
        });
    };

    return (
        <Grid container direction="row" justify="center" alignItems="center" style={{margin: 10}}>
            <Grid xl={8} md={8} xs={8} item>
                <ErrorMessage error={userPageableList.error}/>
                <ZTable data={userPageableList.content} metadata={metadata}
                        handleFetchRequest={handleFetchRequest} pageConfiguration={page}/>
            </Grid>
        </Grid>
    );
}