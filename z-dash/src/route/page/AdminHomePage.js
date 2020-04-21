import React from "react";
import {PersonCard} from "../../component";
import {useSelector} from "react-redux";

export default (...props) => {
    const {username, authorities} = useSelector(state => ({
        username: state.loginResponse.user.subject,
        authorities: state.loginResponse.user.claims.authorities
    }));
    return <PersonCard username={username} authorities={authorities}/>
}