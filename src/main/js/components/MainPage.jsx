import React from 'react';
import LabHeader from './LabHeader';
import PointTable from "./PointTable";
import MainForm from "./MainForm";
import Plot from "./Plot"
import {getAll} from "../api/request";
import store from "../store/store";
import ResizeObserver from "rc-resize-observer";
import {drawPlot} from "../plot/plot";
import ProgressBar from "react-toolbox/lib/progress_bar";

const mainPage = (
    <ResizeObserver onResize={(dimension) => {
        console.log(dimension.width, dimension.height)
        const minWidth = 200;
        const mobileWidth = 805;
        const canvas = document.getElementById('canvas');
        const mainForm = document.getElementById('main-form');
        if (dimension.width < mobileWidth) {
            canvas.width = Math.max(minWidth, dimension.width);
            canvas.height = Math.max(minWidth, dimension.width);
            mainForm
        } else {
            canvas.width = Math.max(mobileWidth, dimension.width / 3);
            canvas.height = Math.max(mobileWidth, dimension.width / 3);
        }
        drawPlot(canvas);
    }}>
        <div className="main-page">
            <LabHeader
                author="Roman Bavykin"
                group="P3210"
                variant="10683"
            />
            <div className='main-content'>
                <Plot/>
                <MainForm/>
            </div>
            <PointTable/>
        </div>
    </ResizeObserver>
)

const loadingPage = (
    <div className='loading-page'>
        <LabHeader
            author='Roman Bavykin'
            group='P3210'
            variant='10683'/>
        <ProgressBar type='linear' mode='indeterminate'/>
    </div>
)

class MainPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            page: loadingPage
        }
    }

    showSnackbar = (msg) => {
        store.dispatch({type: 'snackbar', value: {active: true, label: msg}});
    }

    componentDidMount() {
        getAll().then(response => response.json().then(json => {
                if (response.ok) {
                    store.dispatch({type: "setPoints", value: json});
                    this.setState({page: mainPage})
                } else if (response.status === 401 || response.status === 403) {
                    store.dispatch({type: "login", value: false});
                }   else {
                    this.showSnackbar(json.message);
                }
            }))
    }

    render() {
        return this.state.page;
    }
}

export default MainPage;