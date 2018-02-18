import React from 'react'
import {connect} from "react-redux";
import "./style.css";
import {Link} from "react-router-dom";
import {withRouter} from "react-router";
import {doShowProfile} from "../../actions/profileActions";

class NavigationBar extends React.Component {

    constructor(props) {
        super(props);
        this.toggleSignIn = this.toggleSignIn.bind(this);
        this.onProfileClick = this.onProfileClick.bind(this);
    }

    toggleSignIn() {
        if (this.props.authenticated) {
            this.props.keycloak.logout();
        } else {
            this.props.keycloak.login();
        }
    }

    onProfileClick(e) {
        e.preventDefault();
        this.props.doShowProfile(true);
    }

    render()  {

        const{authenticated} = this.props;

        let signInIconClasses = 'fa-sign-in sign-in-icon';
        let displayNone = 'display-none'
        if (authenticated) {
            signInIconClasses = 'fa-sign-out sign-out-icon';
            displayNone = ''
        }

        return (
            <header>
                <div className="container">
                    <nav className="navbar navbar-expand-lg navbar-light bg-light">
                        <a className="navbar-brand profile-button"></a>
                        <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent">
                            <span className="navbar-toggler-icon"></span>
                        </button>
                        <div className="collapse navbar-collapse" id="navbarSupportedContent">
                            <ul className="navbar-nav">
                                <li className="nav-item">
                                    <Link className="nav-link" to='/'>Home</Link>
                                </li>
                            </ul>
                            <ul className={displayNone+" navbar-nav"}>
                                <li className="nav-item">
                                    <a className="nav-link" onClick={this.onProfileClick}>Profile</a>
                                </li>
                            </ul>
                            <ul className="navbar-nav ml-auto">
                                <li onClick={this.toggleSignIn} className="nav-item">
                                    <i className={"fa " + signInIconClasses}></i>
                                </li>
                            </ul>
                        </div>
                    </nav>
                </div>
            </header>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        keycloak: state.keycloak.keycloak,
        authenticated: state.keycloak.authenticated
    };
}

export default withRouter(connect(mapStateToProps, {doShowProfile})(NavigationBar));