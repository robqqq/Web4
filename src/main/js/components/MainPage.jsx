import React from 'react';
import LabHeader from './LabHeader';
import PointTable from "./PointTable";
import {getAll} from "../api/request";

class MainPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {points: []};
    }

    componentDidMount() {
        getAll().then(response => {
            this.setState({points: response.entity});
        });
    }

    render() {
        return (
            <div className="main-page">
                <LabHeader
                    author="Bavykin Roman"
                    group="P3210"
                    variant="test"
                    />
                <PointTable points={this.state.points}/>
            </div>
        )
    }
}

export default MainPage;