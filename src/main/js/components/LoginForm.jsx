import React from 'react'
import Input from 'react-toolbox/lib/input'
import {Button} from "react-toolbox/lib/button";
import {login, register} from "../api/request";
import store from "../store/store";

class LoginForm extends React.Component {

    state = {
        values: {
            username: '',
            pwd: ''
        },
        errors: {
            username: '',
            pwd: ''
        }
    };

    constructor(props) {
        super(props);
    }

    handleChange = (name, value) => {
        let error = this.inputError(name, value);
        this.setState({...this.state,
            values: {...this.state.values, [name]: value},
            errors: {...this.state.errors, [name]: error}
        });
    };

    showSnackbar = (msg) => {
        store.dispatch({type: 'snackbar', value: {active: true, label: msg}});
    }

    inputError = (name, value) => {
        switch (name) {
            case "username":
                if (value.charAt(0) >= '0' && value.charAt(0) <= '9') {
                    return "Username must starts with a letter";
                }
                if (value.length > 0 && value.length < 4) {
                    return "Username must be at least 4 characters long";
                }
                return ''
            case "pwd":
                if (value.length > 0 && value.length < 6) {
                    return "Password must be at least 6 characters long";
                }
                return "";
            default:
                return '';
        }
    }

    validateInput = () => {
        let username = this.state.values.username;
        let pwd = this.state.values.pwd;
        if (username.length === 0) {
            this.showSnackbar("Username is not set");
            return false;
        }
        if (username.charAt(0) >= '0' && username.charAt(0) <= '9') {
            this.showSnackbar("Username must starts with a letter");
            return false;
        }
        if (username.length > 0 && username.length < 4) {
            this.showSnackbar("Username must be at least 4 characters long");
            return false;
        }
        if (pwd.length === 0) {
            this.showSnackbar("Password is not set");
            return false;
        }
        if (pwd.length > 0 && pwd.length < 6) {
            this.showSnackbar("Password must be at least 6 characters long");
            return false;
        }
        return true;
    }

    login = () => {
        if(this.validateInput()) {
            login(this.state.values.username, this.state.values.pwd).then(response => response.json().then(json => {
                if (response.ok) {
                    sessionStorage.setItem("token", json.accessToken);
                    sessionStorage.setItem("refreshToken", json.refreshToken);
                    store.dispatch({type: "login", value: {login: true, username: this.state.values.username}});
                } else {
                    this.showSnackbar(json.message);
                }
            }))
        }
    }

    register = () => {
        if(this.validateInput()) {
            register(this.state.values.username, this.state.values.pwd).then(response => response.json().then(json => {
                if (response.ok) {
                    this.login();
                } else {
                    this.showSnackbar(json.message);
                }
            }))
        }
    }

    render () {
        return (
            <div className='login-form' style={{width: '50%', marginLeft: 'auto', marginRight: 'auto'}}>
                <Input type='text'
                       label='Username'
                       name='username'
                       value={this.state.values.username}
                       onChange={this.handleChange.bind(this, 'username')}
                       maxLength={20}
                       required
                       error={this.state.errors.username}/>
                <Input type='password'
                       label='Password'
                       name='pwd'
                       value={this.state.values.password}
                       onChange={this.handleChange.bind(this, 'pwd')}
                       maxLength={20}
                       required
                       error={this.state.errors.pwd}/>
                <Button label="Login"
                        onClick={this.login}
                        icon='login'
                        primary/>
                <Button label="Register"
                        onClick={this.register}
                        icon='person_add'
                        primary/>
            </div>
        );
    }
}

export default LoginForm;