import React from 'react';
import DataTable from 'react-data-table-component'
import store from "../store/store";

function capitalFirstChar(str) {
    return str.charAt(0).toUpperCase() + str.slice(1)
}

const tableStyles = {
    head: {
        style: {
            fontSize: '17px',
            fontWeight: 'bold'
        }
    },
    rows: {
        style: {
            fontSize: '17px',
            fontWeight: 'normal'
        }
    }
}

const columns = [
    {
        name: "X",
        selector: row => row.x,
    },
    {
        name: "Y",
        selector: row => row.y,
    },
    {
        name: "R",
        selector: row => row.r,
    },
    {
        name: "Result",
        selector: row => capitalFirstChar(row.result.toString().toString()),
    }
]

class PointTable extends React.Component {

    state = {
        points: []
    }

    constructor(props) {
        super(props);
    }

    componentDidMount() {
        this.setState({points: store.getState().points});
        this.unsubscribe = store.subscribe(() => {
            this.setState({points: store.getState().points});
        });
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

    render() {
        return (
            <div className='table' style={{width: '90%', marginLeft: 'auto', marginRight: 'auto'}}>
                <DataTable
                    className='point-table'
                    columns={columns}
                    data={this.state.points}
                    noDataComponent={null}
                    customStyles={tableStyles}/>
            </div>
        )
    }
}

export default PointTable;