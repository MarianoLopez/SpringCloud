import React, {useEffect, useRef} from "react";
import {fromEvent} from "rxjs";
import {debounceTime, distinctUntilChanged, map} from "rxjs/operators";
import InputBase from "@material-ui/core/InputBase";

export default ({onChange, dueTime = 500, ...rest}) => {
    const el = useRef(null);

    const subscribe = () => {
        fromEvent( el.current, 'input')
            .pipe(
                map(event => event.target.value),
                debounceTime(dueTime),
                distinctUntilChanged()
            )
            .subscribe({
                next: (value => onChange(value))
            });
    };

    useEffect(subscribe, []);

    return (
        <InputBase {...rest} ref={el}/>
    );
};