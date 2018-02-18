import React from 'react'
import {getAction} from "../../actions/serverActions";
import {connect} from "react-redux";
import {withRouter} from "react-router";

class Home extends React.Component {

    constructor(props) {
        super(props);
        this.secured = this.secured.bind(this);
        this.unsecured = this.unsecured.bind(this);

        this.state = {
            unsecured: ""
            ,secured: ""
        }
    }

    secured() {
        getAction('/test/secure', this.props.keycloak.token).then((response) => {
            response.json().then((jsonValue) => {
                this.setState({
                    secured: jsonValue.textValue
                })
            });
        });

    }

    unsecured() {
        getAction('/test').then((response) => {
            response.json().then((jsonValue) => {
                this.setState({
                    unsecured: jsonValue.textValue
                })
            });
        });
    }

    render() {
        return (
            <div className="loggedin">
                <h2>Home</h2>
                <button onClick={this.unsecured}>Try to get unsecured content</button>&nbsp;&nbsp;&nbsp;&nbsp;<span>{this.state.unsecured}</span>
                <br/><br/>
                <button onClick={this.secured}>Try to get secured content</button>&nbsp;&nbsp;&nbsp;&nbsp;<span>{this.state.secured}</span>
            </div>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        keycloak: state.keycloak.keycloak,
        authenticated: state.keycloak.authenticated
    };
}

export default withRouter(connect(mapStateToProps, {})(Home));
