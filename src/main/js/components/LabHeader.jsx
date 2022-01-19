import React from 'react';

class LabHeader extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="lab-header">
                <p className="header-author">{this.props.author}, {this.props.group}</p>
                <p className="header-variant">{this.props.variant}</p>
            </div>
        )
    }
}

export default LabHeader;