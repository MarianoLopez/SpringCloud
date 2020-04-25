import React, {useState} from "react";
import {Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@material-ui/core";
import {getKey} from "../../utils/reactUtils";
import TableSortLabel from "@material-ui/core/TableSortLabel";
import makeStyles from "@material-ui/core/styles/makeStyles";
import TablePagination from "@material-ui/core/TablePagination";
import {EnhancedTableToolbar} from "../surface/EnhancedTableToolbar";


const useStyles = makeStyles(() => ({
    visuallyHidden: {
        border: 0,
        clip: 'rect(0 0 0 0)',
        height: 1,
        margin: -1,
        overflow: 'hidden',
        padding: 0,
        position: 'absolute',
        top: 20,
        width: 1,
    },
}));

export const ZTable = ({title, configuration, data, isLoading = false, onChange}) => {
    const classes = useStyles();
    const sortConfig = configuration.page.sort.split(",");

    const [page, setPage] = useState({
       sort: {
           by: sortConfig[0],
           direction: sortConfig[1]
       },
       number: configuration.page.number,
       size: configuration.page.size
    });

    const handleOrderRequest = (field) => {
        let isAsc = page.sort.by === field && page.sort.direction === 'asc';
        let _sort = {
            direction: isAsc ? 'desc' : 'asc',
            by: field
        };

        let nextState = {
            ...page,
            sort: _sort
        };
        setPage(nextState);
        onChange(nextState);
    };

    const onChangePage = (event, pageNumber) => {
        let nextState = {
            ...page,
            number: pageNumber
        };
        setPage(nextState);
        onChange(nextState);
    };
    
    const onChangeRowsPerPage = (event, select) => {
        let pageSize = parseInt(select.key, 10);

        let nextState = {
            ...page,
            size: pageSize
        };
        setPage(nextState);
        onChange(nextState);
    };

    const isSortingBy = (field) => {
        return page.sort.by === field.toString();
    };

    const ZTableHead = () => {
        const cells = configuration.row.map(cell => {
            return (
                <TableCell
                    key={cell.name}
                    align="center"
                    sortDirection={isSortingBy(cell.field) ? page.sort.direction : false}>
                    <TableSortLabel
                        active={isSortingBy(cell.field)}
                        direction={isSortingBy(cell.field) ? page.sort.direction : 'asc'}
                        onClick={() => handleOrderRequest(cell.field)}>
                        {cell.name}
                        {isSortingBy(cell.field) ?
                            <span className={classes.visuallyHidden}>
                                    {page.sort.direction === 'desc' ? 'sorted descending' : 'sorted ascending'}
                                </span>
                            : null
                        }
                    </TableSortLabel>
                </TableCell>
            );
        });
        return (
            <TableHead>
                <TableRow>{cells}</TableRow>
            </TableHead>
        );
    };

    const DataRow = ({data}) => {
        return configuration.row.map((cell, index) => {
            return (
                <TableCell
                    align="center"
                    key={getKey(cell.field, data.id)}
                    component={index === 0 ? "th" : undefined}
                    scope={index === 0 ? "row" : undefined}>
                    {!isLoading? cell.render(data[cell.field], data.id) : cell.skeleton()}
                </TableCell>
            );
        })
    };

    const ZTableBody = () => {
        const body = data.content.map((row) => (
            <TableRow key={getKey('userRow', row.id)}>
                <DataRow data={row}/>
            </TableRow>
        ));
        return <TableBody>{body}</TableBody>
    };

    return (
        <Paper>
            <EnhancedTableToolbar title={title}/>
            <TableContainer>
                <Table aria-label="simple table">
                    <ZTableHead/>
                    <ZTableBody/>
                </Table>
            </TableContainer>
            <TablePagination
                rowsPerPageOptions={configuration.page.rowsPerPageOptions}
                component="div"
                count={data.totalElements}
                rowsPerPage={page.size}
                page={page.number}
                onChangePage={onChangePage}
                onChangeRowsPerPage={onChangeRowsPerPage}
            />
        </Paper>
    );
};