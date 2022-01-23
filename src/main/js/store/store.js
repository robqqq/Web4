import {createStore} from "redux";

const initialState = {
    login: sessionStorage.getItem("login"),
    points: [],
    r: null,
    snackbar: {
        active: false,
        label: ''
    }
};

function reducer (state, action) {
    switch (action.type) {
        case "login":
            sessionStorage.setItem("login", action.value.login);
            return {...state,
                r: action.value.login === null ? null : state.r,
                login: action.value.login,
                points: []};
        case "appendPoint":
            return {...state, points: [...state.points, action.value]};
        case "changeR":
            return {...state, r: action.value}
        case "setPoints":
            sessionStorage.setItem("points", action.value);
            return {...state, points: action.value}
        case "snackbar":
            return {...state, snackbar: {active: action.value.active, label: action.value.label}}
        default:
            return state;
    }
}

const store = createStore(reducer, initialState);
export default store;