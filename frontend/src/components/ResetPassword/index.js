import React from 'react'
import PropTypes from 'prop-types';
import {connect} from 'react-redux'
import {Link, withRouter} from 'react-router-dom'

import validator from 'validator';

import {websiteTitle} from '../../conf/application.properties'

import {resetPassword} from '../../utils/AuthUtils'
import * as queryString from "query-string";

class ResetPassword extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            password: '',
            repeatpassword: '',
            errors: {},
            isLoading: false
        };

        this.onChange = this.onChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.validateInput = this.validateInput.bind(this);
    }

    componentWillMount() {
        const {isAuthenticated} = this.props;
        if (isAuthenticated) {
            this.props.history.push('/loggedin');
        }
    }

    componentDidUpdate() {
        const {isAuthenticated} = this.props;
        if (isAuthenticated) {
            this.props.history.push('/loggedin');
        }
    }

    onChange(e) {
        this.setState({[e.target.name] : e.target.value});
    }

    validateInput(formData) {
        let errors = {};
        if (validator.isEmpty(formData.password)) errors.password = "You have to enter a password";
        if (formData.password !== formData.repeatpassword) errors.repeatpassword = "Passwords are not equal";

        this.setState({errors});

        return !errors.password && !errors.repeatpassword;
    }

    onSubmit(e) {
        e.preventDefault();
        if (this.validateInput(this.state)) {
            this.setState({errors:{}, isLoading:true});
            resetPassword({newPassword: this.state.password, token: queryString.parse(this.props.location.search)['token']}).then(
                () => {
                    this.props.history.push('/');
                },
                (response) => {
                    response.json().then(function(json) {
                        this.setState({isLoading:false, errors: {server: json.description}});
                    }.bind(this))
                }
            );
        }
    }

    render() {

        const {errors} = this.state;

        return (
            <div className="login-box">
                <div className="login-logo">
                    <Link to="/">{websiteTitle}</Link>
                </div>
                <div className="login-box-body">
                    <p className="login-box-msg">Reset your password</p>

                    {
                        errors &&
                        errors.server &&
                        <p className="login-server-error">
                            {errors.server}
                        </p>
                    }

                    <form onSubmit={this.onSubmit} method="post">
                        <div className="form-group has-feedback">
                            <input onChange={this.onChange} type="password" className="form-control" placeholder="Password" name="password" />
                            <span className="glyphicon glyphicon-lock form-control-feedback"></span>
                            {errors && errors.password && <span className="form-input-error-message">{errors.password}</span>}
                        </div>
                        <div className="form-group has-feedback">
                            <input onChange={this.onChange} type="password" className="form-control" placeholder="Repeat Password" name="repeatpassword" />
                            <span className="glyphicon glyphicon-lock form-control-feedback"></span>
                            {errors && errors.repeatpassword && <span className="form-input-error-message">{errors.repeatpassword}</span>}
                        </div>
                        <div className="row">
                            <div className="col-xs-6"></div>
                            <div className="col-xs-6">
                                <button disabled={this.state.isLoading} type="submit" className="btn btn-primary btn-block btn-flat">Reset Password</button>
                            </div>
                        </div>
                    </form>

                    <br/>
                    <Link to="/"><span className="text-center">I didn't forget my password, go to login</span></Link><br/>
                    <Link to="/signup"><span className="text-center">Register a new membership</span></Link>

                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        isAuthenticated: state.auth.isAuthenticated
    };
}

ResetPassword.contextTypes = {
    router: PropTypes.object.isRequired
}

export default connect(mapStateToProps)(withRouter(ResetPassword));