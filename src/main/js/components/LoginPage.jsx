import React from "react";
import store from "../store/store";
import {login, register} from "../api/request";
import LabHeader from "./LabHeader";
import LoginForm from "./LoginForm";

class LoginPage extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return(
            <div className='login-page'>
                <LabHeader
                    author='Roman Bavykin'
                    group='P3210'
                    variant='10683'/>
                <LoginForm/>
            </div>
        )
    }
}

export default LoginPage;