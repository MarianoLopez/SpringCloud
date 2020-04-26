import {Grid} from "@material-ui/core";
import React, {useEffect, useState} from "react";
import {findAll} from "../../../service/userService";
import {ZTable} from "../../../component/table/ZTable";
import tableConfiguration from "./userTableConfiguration";
import {ErrorMessage} from "../../../component";

const defaultResponse = {
    data: {
        content: [],
        totalElements: 0
    },
    error: null,
    isLoading: false
};

export default () => {
    const [page, setPage] = useState({
        number: 0,
        size: 10,
        sort: 'name,asc',
        search: '',
        rowsPerPageOptions: [5, 10, 15, 20, { value: -1, label: 'All' }]
    });
    const [response, setResponse] = useState(defaultResponse);

    const fetchData = () => {
        setResponse({
            ...response,
            isLoading: true
        });
        const {number, size, sort, search} = page;
        findAll(number, size, sort, search)
            .then(res => {
                setResponse({
                    error: null,
                    data: res.data,
                    isLoading: false
                });
            })
            .catch(err => {
                setResponse({
                    ...response,
                    isLoading: false,
                    error: typeof err.response != 'undefined' ? err.response.data : err.message,
                });
            })
    };

    useEffect(fetchData, [page]);

    const handleFetchRequest = (_page) => {
        const {size, number, search, sort} = _page;
        setPage({
            ...page,
            size, number, search,
            sort: `${sort.by},${sort.direction}`
        });
    };

    return (
        <Grid container direction="row" justify="center" alignItems="center" style={{margin: 10}}>
            <Grid xl={8} md={8} xs={8} item>
                <ErrorMessage error={response.error}/>
                <ZTable title="Users"
                        configuration={{row: tableConfiguration, page: page}}
                        data={response.data}
                        isLoading={response.isLoading}
                        onChange={handleFetchRequest}
                        stickyHeader={true}/>
            </Grid>
        </Grid>
    );
}