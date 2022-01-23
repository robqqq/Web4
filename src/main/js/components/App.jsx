import React from 'react';
import MainPage from "./MainPage";

import store from "../store/store";
import LoginPage from "./LoginPage";
import Snackbar from "react-toolbox/lib/snackbar";

class App extends React.Component {

    componentDidMount() {
        store.subscribe(() => {
            this.setState({reduxState: store.getState()});
        });
    }

    hideSnackbar = () => {
        store.dispatch({type: 'snackbar', value: {active: false, label: ''}});
    }

    render() {
        return (
            <div className="app">
                {store.getState().login && store.getState().login != null && store.getState().login !== undefined ?
                    <MainPage/> : <LoginPage/>}
                <Snackbar
                    action='OK'
                    active={store.getState().snackbar.active}
                    label={store.getState().snackbar.label}
                    timeout={2000}
                    onClick={this.hideSnackbar}
                    onTimeout={this.hideSnackbar}
                    type='cancel'/>
            </div>
        )
    }
}

export default App;