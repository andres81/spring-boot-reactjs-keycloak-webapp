import React from 'react'
import {connect} from 'react-redux'
import {logout} from '../../actions/authActions'
import UserDropDown from './UserProfileModal'

import "./style.css";
import withRouter from "react-router-dom/es/withRouter";
import {Link} from "react-router-dom";

class NavigationBar extends React.Component {

    render()  {

        let logOutButton = '';
        if (this.props.isAuthenticated) {
            logOutButton =
                <a onClick={this.props.logout}>
                    <i className="fa fa-sign-out sign-out-icon"></i>
                </a>
        }

        const {admin, permissions} = this.props.user;

        let isAdmin = admin ? true : false;
        if (!isAdmin && permissions && permissions !== null) {
            isAdmin = permissions.length !== 0;
        }

        return (
            <header>
                <div className="container">
                    <nav className="navbar navbar-expand-lg navbar-light bg-light">
                        <a className="navbar-brand profile-button"><UserDropDown/></a>
                        <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent">
                            <span className="navbar-toggler-icon"></span>
                        </button>

                        <div className="collapse navbar-collapse" id="navbarSupportedContent">
                            <ul className="navbar-nav mr-auto">
                                <li className="nav-item">
                                    <Link className="nav-link" to='/'>Home</Link>
                                </li>
                                {isAdmin && <li><Link className="nav-link" to='/admin'>Admin</Link></li>}
                            </ul>
                            {logOutButton}
                        </div>
                    </nav>
                </div>
            </header>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        isAuthenticated: state.auth.isAuthenticated,
        user: state.user
    };
}

export default withRouter(connect(mapStateToProps, {logout})(NavigationBar));