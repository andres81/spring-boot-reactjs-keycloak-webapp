import React from 'react';

import NavigationBar from './components/NavigationBar'
import Home from "./components/Home";
import Footer from "./Footer";
import './css/App.css';
import {Route, Switch, withRouter} from "react-router";
import {connect} from "react-redux";
import {setAuthenticated} from "./actions/keycloakActions";
import Profile from './components/Profile'
import modal from "./utils/modal";
import {doShowProfile} from "./actions/profileActions";

class App extends React.Component {

    componentDidMount() {
        this.props.keycloak.init(
            {
                onLoad: 'check-sso',
                checkLoginIframeInterval: 1
            }
        ).success(()=>this.props.setAuthenticated(this.props.keycloak.authenticated));
    }

    render() {
        const {showProfile} = this.props;
        return (
            <div className="App">
                <NavigationBar />
                <Switch>
                    <Route exact path='/' component={Home}/>
                </Switch>
                {showProfile && modal(Profile, () => this.props.doShowProfile(false))}
                <Footer/>
            </div>
        );
    }
};

const mapStateToProps = (state) => {
    return {
        keycloak: state.keycloak.keycloak
        ,showProfile: state.profile.showProfile
    };
}

export default withRouter(connect(mapStateToProps, {setAuthenticated, doShowProfile})(App));