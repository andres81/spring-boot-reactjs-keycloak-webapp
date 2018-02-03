import React from 'react';
import PropTypes from 'prop-types';
import {connect} from 'react-redux';
import withRouter from "react-router-dom/es/withRouter";

export default function (ProtectedComponent) {
    class Authenticate extends React.Component {

        componentWillMount() {
            if (!this.props.isAuthenticated) {
                this.props.history.push('/');
            }
        }

        componentWillUpdate(props) {
            if (!props.isAuthenticated) {
                this.props.history.push('/');
            }
        }

        render() {
            return (
                <ProtectedComponent {...this.props} />
            );
        }

    }

    Authenticate.contextTypes = {
        router: PropTypes.object.isRequired
    }

    function mapStateToProps(state) {
        return {
            isAuthenticated: state.auth.isAuthenticated
        };
    }

    return withRouter(connect(mapStateToProps)(Authenticate));
}
