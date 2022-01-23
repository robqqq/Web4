import React from 'react';
import store from "../store/store";
import {clicked} from "../plot/plot";

class Plot extends React.Component {

    showSnackbar = (msg) => {
        store.dispatch({type: 'snackbar', value: {active: true, label: msg}});
    }

    render() {
        return (
            <div id='plot' style={{width: "fit-content", marginLeft: 'auto', marginRight: 'auto'}}>
                <canvas id='canvas' width={2000} height={2000} onClick={e => {
                    let err = clicked(e);
                    if (err !== '') {
                        this.showSnackbar(err);
                    }
                }}/>
            </div>
        )
    }
}

export default Plot;