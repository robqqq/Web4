import React from 'react';
import Input from 'react-toolbox/lib/input'
import {Dropdown} from "react-toolbox/lib/dropdown";
import {Button} from "react-toolbox/lib/button";
import {check, clear} from "../api/request";
import store from "../store/store";
import {drawPlot} from "../plot/plot";

const xValues = [
    {value: -2, label: '-2'},
    {value: -1.5, label: '-1.5'},
    {value: -1, label: '-1'},
    {value: -0.5, label: '-0.5'},
    {value: 0, label: '0'},
    {value: 0.5, label: '0.5'},
    {value: 1, label: '1'},
    {value: 1.5, label: '1.5'},
    {value: 2, label: '2'}
];
const rValues = [
    {value: 0.5, label: '0.5'},
    {value: 1, label: '1'},
    {value: 1.5, label: '1.5'},
    {value: 2, label: '2'}
];

class MainForm extends React.Component {
    state = {
        values: {
            x: '',
            y: '',
        },
        errors: {
            x: '',
            y: '',
            r: ''
        },
        snackbar: {
            active: false,
            label: ''
        }
    }

    handleChange = (name, value) => {
        let error = this.inputError(name, value);
        if (name === 'r') {
            store.dispatch({type: 'changeR', value: value});
            this.setState({...this.state, errors: {...this.state.errors, r: error}});
            drawPlot(document.getElementById('canvas'));
            return;
        }
        this.setState({...this.state,
            values: {...this.state.values, [name]: value},
            errors: {...this.state.errors, [name]: error},
        })
    }

    inputError = (name, value) => {
        switch (name) {
            case 'x':
                if (isNaN(value)){
                    return "X must be a number";
                }
                if (value < -2 || value > 2) {
                    return "X value must be in [-2; 2]";
                }
                return "";
            case 'y':
                if (isNaN(value)){
                    return "Y must be a number";
                }
                if (value <= -5 || value >= 5) {
                    return "Y value must be in (-5; 5)";
                }
                return "";
            case 'r':
                if (isNaN(value)){
                    return "R must be a number";
                }
                if (value <= 0 || value > 2) {
                    return "R value must be in (0; 2]";
                }
                return "";
            default:
                return "";
        }
    }

    validateInput = () => {
        let x = this.state.values.x;
        let y = this.state.values.y;
        let r = store.getState().r
        if (x === "") {
            this.showSnackbar("X is not set")
            return false;
        }
        if (isNaN(x)){
            this.showSnackbar("X must be a number");
            return false;
        }
        if (x < -2 || x > 2) {
            this.showSnackbar("X value must be in [-2; 2]");
            return false;
        }
        if (y === "") {
            this.showSnackbar("Y is not set")
            return false;
        }
        if (isNaN(y)){
            this.showSnackbar("Y must be a number");
            return false;
        }
        if (y <= -5 || y >= 5) {
            this.showSnackbar("Y value must be in (-5; 5)");
            return false;
        }
        if (r === "" || r === null) {
            this.showSnackbar("R is not set")
            return false;
        }
        if (isNaN(r)){
            this.showSnackbar("R must be a number");
            return false;
        }
        if (r <= 0 || r > 2 ) {
            this.showSnackbar("R value must be in (0; 2]");
            return false;
        }
        return true;
    }

    check = () => {
        if (this.validateInput()) {
            check({x: this.state.values.x, y: this.state.values.y, r: store.getState().r})
                .then(response => response.json().then(json => {
                if (response.ok) {
                    store.dispatch({type: "appendPoint", value: json});
                    drawPlot(document.getElementById('canvas'));
                } else if (response.status === 401 || response.status === 403){
                    store.dispatch({type: 'login', value: true});
                } else {
                    this.showSnackbar(json.message);
                }
            }))
        }
    }

    clear = () => {
        clear({x: this.state.values.x, y: this.state.values.y, r: store.getState().r})
            .then(response => response.json().then(json => {
                if (response.ok) {
                    store.dispatch({type: "setPoints", value: []});
                    drawPlot(document.getElementById('canvas'));
                } else if (response.status === 401 || response.status === 403){
                    store.dispatch({type: 'login', value: true});
                } else {
                    this.showSnackbar(json.message);
                }
            }))
    }

    showSnackbar = (msg) => {
        store.dispatch({type: 'snackbar', value: {active: true, label: msg}});
    }

    render () {
        return (
            <div id='main-form' style={{width: '50%', marginLeft: 'auto', marginRight: 'auto'}}>
                <Dropdown
                    auto
                    label='X'
                    name='x'
                    onChange={this.handleChange.bind(this, 'x')}
                    source={xValues}
                    value={this.state.values.x}
                    error={this.state.errors.x}
                    required/>
                <Input
                    type='text'
                    label='Y'
                    name='y'
                    value={this.state.yValue}
                    onChange={this.handleChange.bind(this, 'y')}
                    maxLength={12}
                    required
                    error={this.state.errors.y}/>
                <Dropdown
                    auto
                    label='R'
                    name='r'
                    onChange={this.handleChange.bind(this, 'r')}
                    source={rValues}
                    value={store.getState().r}
                    error={this.state.errors.r}
                    required/>
                    <Button label="Add"
                            onClick={this.check}
                            icon='add'
                            primary/>
                    <Button label="Clear"
                            onClick={this.clear}
                            icon='clear_all'
                            primary/>
            </div>
        )
    }
}

export default MainForm;