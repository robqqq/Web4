import React from 'react';
import Table from 'react-toolbox/lib/table'

const PointModel = {
    x: {type: Number},
    y: {type: Number},
    r: {type: Number},
    result: {type: Boolean}
};

class PointTable extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Table
                model={PointModel}
                source={this.props.points}/>
        )
    }
}

export default PointTable;