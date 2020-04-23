import React, {useState} from "react";
import {Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@material-ui/core";
import {getKey} from "../../utils/reactUtils";
import TableSortLabel from "@material-ui/core/TableSortLabel";
import makeStyles from "@material-ui/core/styles/makeStyles";


const useStyles = makeStyles((theme) => ({
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


export const ZTable = ({data, metadata, handleFetchRequest, pageConfiguration}) => {
    const classes = useStyles();
    const sortConfig = pageConfiguration.sort.split(",");

    const [sort, setSort] = useState({
        by: sortConfig[0],
        direction: sortConfig[1]
    });

    const handleOrderRequest = (field) => {
        let isAsc = sort.by === field && sort.direction === 'asc';
        let _sort = {
            direction: isAsc ? 'desc' : 'asc',
            by: field
        };
        setSort(_sort);
        handleFetchRequest(_sort);
    };

    const isSortingBy = (field) => {
        return sort.by === field.toString();
    };

    const ZTableHead = () => {
        const cells = metadata.map(cell => {
                return (
                    <TableCell
                        key={cell.name}
                        align="center"
                        sortDirection={isSortingBy(cell.field) ? sort.direction : false}>
                        <TableSortLabel
                            active={isSortingBy(cell.field)}
                            direction={isSortingBy(cell.field) ? sort.direction : 'asc'}
                            onClick={() => handleOrderRequest(cell.field)}>
                            {cell.name}
                            {isSortingBy(cell.field) ?
                                <span className={classes.visuallyHidden}>
                                    {sort.direction === 'desc' ? 'sorted descending' : 'sorted ascending'}
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
        return metadata.map((cell, index) => {
            return (
                <TableCell
                    align="center"
                    key={getKey(cell.field, data.id)}
                    component={index === 0 ? "th" : undefined}
                    scope={index === 0 ? "row" : undefined}>
                    {cell.render(data[cell.field], data.id)}
                </TableCell>
            );
        })
    };

    const ZTableBody = () => {
        const body = data.map((row) => (
            <TableRow key={getKey('userRow', row.id)}>
                <DataRow data={row}/>
            </TableRow>
        ));
        return <TableBody>{body}</TableBody>
    };

    return (
        <TableContainer component={Paper}>
            <Table aria-label="simple table">
                <ZTableHead/>
                <ZTableBody/>
            </Table>
        </TableContainer>
    );
};