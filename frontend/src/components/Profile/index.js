import React from 'react'
import {connect} from 'react-redux'

import './style.css'

class Profile extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {

        const {keycloak} = this.props;

        return (
            <div>
                <h3>Profile</h3>
                <div className="profile">
                    <label className="profile-input-label">Email</label>
                    <br/>
                    <input className="profile-input" value={keycloak.tokenParsed['email']} disabled data-field="email"/>

                    <label className="profile-input-label">First name</label>
                    <br/>
                    <input className="profile-input" value={keycloak.tokenParsed['given_name']} disabled data-field="fullName"/>

                    <label className="profile-input-label">Last name</label>
                    <br/>
                    <input className="profile-input" value={keycloak.tokenParsed['family_name']} disabled data-field="fullName"/>

                    <label className="profile-input-label">Username</label>
                    <br/>
                    <input className="profile-input" value={keycloak.tokenParsed['preferred_username']} disabled data-field="fullName"/>

                    <br/><br/>

                    <a className="account-management-link" onClick={() => this.props.keycloak.accountManagement()}>Account management</a>
                </div>
            </div>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        keycloak: state.keycloak.keycloak
    };
}

export default connect(mapStateToProps, {})(Profile);