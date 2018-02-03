import React from 'react'
import {Link} from 'react-router-dom'
import {backendUrl} from '../../conf/application.properties'
import queryString from 'query-string'
import {withRouter} from 'react-router-dom'

class ConfirmSignup extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            error:""
        }
    }

    componentWillMount() {

        var token = queryString.parse(this.props.location.search)['token'];

        fetch(backendUrl+'/auth/validateUserAccount?token='+token, {method : "PUT"})
            .then(function(response) {
                if (response.ok) this.props.history.push('/');
                response.text().then(function(error) {
                    this.setState({error: error});
                }.bind(this));
            }.bind(this));
    }

    render() {
        const {error} = this.state;

        return (
            <div className="login-box">
                <h1>{error}</h1>
                <Link to="/"><span className="text-center">Back to login</span></Link>
            </div>
        )
    }
}

export default withRouter(ConfirmSignup);