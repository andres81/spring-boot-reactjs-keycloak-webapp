import React from 'react'

import './style.css'
import withRouter from "react-router-dom/es/withRouter";
import {connect} from "react-redux";
import UserList from "./UserList";
import {getAction} from "../../actions/serverActions";

class Admin extends React.Component {

    constructor(props) {
        super(props);
        this.retrieveUserPermissions = this.retrieveUserPermissions.bind(this);

        this.state = {
            permissions: []
        }
    }

    render() {

        const {admin, permissions} = this.props.user;
        const containers = [];
        let index = 0;
        let userListPermission = false;
        if (permissions) {
            permissions.forEach(permission => {
                if (permission.name === 'user_list') userListPermission = true
            });
        }
        if (admin || userListPermission) {
            containers.push(<UserList key={++index}/>);
        }

        return (
            <div className="container">
                <div className="row before-header"></div>
                <div className="row">
                    <h2 className="admin-title full-width text-center">Administration</h2>
                </div>
                <div className="row between-rows"></div>
                {containers}
                <div className="row before-footer"></div>
            </div>
        );
    }

    retrieveUserPermissions() {
        getAction(this.props.dispatch, 'user/'+this.props.user.id+'/permissions')
            .then(json => {
                this.setState({
                    permissions: json
                })
            });
    }
}

const mapStateToProps = (state) => {
    return {
        user: state.user
    };
}

export default withRouter(connect(mapStateToProps, {})(Admin));

