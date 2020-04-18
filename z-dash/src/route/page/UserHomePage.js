import React from "react";
import {useSelector} from "react-redux";
import {PersonCard} from "../../component";

export default (...props) => {
    const {username, authorities} = useSelector(state => ({
        username: state.loginResponse.user.subject,
        authorities: state.loginResponse.user.claims.authorities
    }));
    return <PersonCard username={username} authorities={authorities}/>
}