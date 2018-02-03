import React from 'react';

import NavigationBar from './components/NavigationBar'
import Home from "./components/Home";
import Footer from "./Footer";

import './css/App.css';
import {Route, Switch, withRouter} from "react-router";
import {retrieveUserInfo} from "./actions/userActions";
import {connect} from "react-redux";
import Admin from "./components/Admin";
import requireAuth from "./utils/requireAuth";

class App extends React.Component {

    componentDidMount() {
        if (this.props.isAuthenticated) {
            this.props.retrieveUserInfo();
        }
    }

    render() {
        const {user} = this.props;
        return (
            <div className="App">
                <NavigationBar user={user} />
                <Switch>
                    <Route exact path='/' component={Home}/>
                    <Route path='/admin' component={requireAuth(Admin)} />
                </Switch>
                <Footer/>
            </div>
        );
    }
};

const mapStateToProps = (state) => {
    return {
        isAuthenticated: state.auth.isAuthenticated,
        user: state.user
    };
}

export default withRouter(connect(mapStateToProps, {retrieveUserInfo})(App));