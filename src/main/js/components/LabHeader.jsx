import React from 'react';
import AppBar from "react-toolbox/lib/app_bar";
import store from "../store/store";

class LabHeader extends React.Component {

    constructor(props) {
        super(props);
    }

    logout = () => {
        store.dispatch({type: 'login', value: {login: false, username: ""}});
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('refreshToken');
    }

    render() {
        let title = this.props.author + ', ' + this.props.group + ', V-' + this.props.variant;
        return (
            <AppBar
                title={title}
                // flat
                rightIcon={store.getState().login && store.getState().login != null ? "logout" : ""}
                onRightIconClick={this.logout}>
            </AppBar>
        )
    }
}

export default LabHeader;