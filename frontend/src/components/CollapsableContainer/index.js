import React from 'react'

import './style.css'

class CollapsableContainer extends React.Component {

    constructor(props) {
        super(props);

        this.toggleCollapse = this.toggleCollapse.bind(this);

        this.state = {
            collapsed : true
            ,collapsedClass : 'collapsed'
        }
    }

    toggleCollapse() {
        if (this.state.collapsed) {
            this.setState(
                {
                    collapsedClass: 'uncollapsed'
                    ,collapsed:false
                }
            );
        } else {
            this.setState(
                {
                    collapsedClass: 'collapsed'
                    ,collapsed:true
                }
            );
        }
    }

    render() {
        return (
            <div className={"collapsable-container"}>
                <div onClick={this.toggleCollapse} className={'collapsable-header'}>
                    <h3>
                        <i className={"fa fa-caret-down collapse-icon" + (this.state.collapsed ? "" : ' turn180')} ></i>
                        &nbsp;&nbsp;&nbsp;
                        {this.props.headerTitle}
                    </h3>
                </div>
                <div className={'collapsable ' + this.state.collapsedClass}>
                    {this.props.children}
                </div>
            </div>
        )
    }
}

export default CollapsableContainer;
