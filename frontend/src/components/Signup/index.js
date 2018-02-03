import React from 'react';
import PropTypes from 'prop-types';
import Modal from 'react-modal'
import {connect} from 'react-redux'
import {Link, withRouter} from 'react-router-dom'
import {signup} from '../../utils/AuthUtils'
import {Checkbox} from 'react-icheck';
import validator from 'validator';
import SignupConfirmed from './SignupConfirmed';

import 'icheck/skins/all.css';

import {websiteTitle} from '../../conf/application.properties'

class Signup extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            fullName: '',
            email: '',
            password: '',
            repassword: '',
            modalIsOpen: false,
            termsagreed: false,
            errors: {},
            isLoading: false
        };

        this.onChange = this.onChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.validateInput = this.validateInput.bind(this);
        this.openModal = this.openModal.bind(this);
        this.closeModal = this.closeModal.bind(this);
    }

    onChange(e) {
        let name = e.target.name;
        this.setState({[name] : name  === 'termsagreed' ? e.target.checked : e.target.value});

    }

    validateInput(formData) {
        let errors = {};
        if (validator.isEmpty(formData.fullName)) errors.fullName = "Enter a name";
        if (!validator.isEmail(formData.email)) errors.email = "Not a valid email address";
        if (validator.isEmpty(formData.password)) errors.password = "Enter a password";
        if (formData.password !== formData.repassword) {
            errors.repassword = "Passwords do not match!";
        }
        if (!formData ||
                formData === null ||
                !formData.termsagreed)
            errors.termsagreed = "You have to agree to the terms of service!";

        this.setState({errors});

        return Object.keys(errors).length === 0;
    }

    onSubmit(e) {
        e.preventDefault();
        if (this.validateInput(this.state)) {
            this.setState({errors:{}, isLoading:true});
            signup({...this.state}).then(
                () => {
                    this.openModal()
                }, (json) => {
                    if (json && json.error) {
                        this.setState({isLoading: false, errors: {server: json.description}});
                    } else {
                        this.setState({
                            isLoading: false,
                            errors: {server: "The server was not able to handle the request, please try again later..."}
                        });
                    }
                }
            );
        }
    }

    openModal() {
        this.setState({modalIsOpen: true});
    }

    closeModal() {
        this.setState({modalIsOpen: false});
        this.props.history.push('/');
    }

    render() {

        const {errors, modalIsOpen} = this.state;

        return (
            <div className="register-box">
                <div className="register-logo">
                    <Link to="/">{websiteTitle}</Link>
                </div>

                <div className="register-box-body">
                    <p className="login-box-msg">Signup a new membership</p>

                    {
                        errors &&
                        errors.server &&
                        <p className="form-input-error-message">
                            {errors.server}
                        </p>
                    }

                    <form onSubmit={this.onSubmit} method="post">
                        <div className="form-group has-feedback">
                            <input onChange={this.onChange} type="text" className="form-control" placeholder="Full name" name="fullName" />
                            <span className="glyphicon glyphicon-user form-control-feedback"></span>
                            {errors && errors.fullName && <p className="form-input-error-message">{errors.fullName}</p>}
                        </div>
                        <div className="form-group has-feedback">
                            <input onChange={this.onChange} type="email" className="form-control" placeholder="Email" name="email" />
                            <span className="glyphicon glyphicon-envelope form-control-feedback"></span>
                            {errors && errors.email && <p className="form-input-error-message">{errors.email}</p>}
                        </div>
                        <div className="form-group has-feedback">
                            <input onChange={this.onChange} type="password" className="form-control" placeholder="Password" name="password" />
                            <span className="glyphicon glyphicon-lock form-control-feedback"></span>
                            {errors && errors.password && <p className="form-input-error-message">{errors.password}</p>}
                        </div>
                        <div className="form-group has-feedback">
                            <input onChange={this.onChange} type="password" className="form-control" placeholder="Retype password" name="repassword" />
                            <span className="glyphicon glyphicon-log-in form-control-feedback"></span>
                            {errors && errors.repassword && <p className="form-input-error-message">{errors.repassword}</p>}
                        </div>
                        <div className="row">
                            <div className="col-xs-8">
                                <div className="checkbox icheck">
                                    <Checkbox
                                        onChange={this.onChange}
                                        checkboxClass="icheckbox_square-blue"
                                        name="termsagreed"
                                        label=" I agree to the " />
                                    <a href="#termsAndConditions" data-toggle="modal"> terms</a>
                                    {errors && errors.termsagreed && <p className="form-input-error-message">{errors.termsagreed}</p>}
                                </div>
                            </div>
                            <div className="col-xs-4">
                                <button type="submit" className="btn btn-primary btn-block btn-flat">Signup</button>
                            </div>
                        </div>
                    </form>

                    <div className="social-auth-links text-center">
                        <p>- OR -</p>
                        <a  className="btn btn-block btn-social btn-facebook btn-flat"><i className="fa fa-facebook"></i> Sign up using
                            Facebook</a>
                        <a  className="btn btn-block btn-social btn-google btn-flat"><i className="fa fa-google-plus"></i> Sign up using
                            Google+</a>
                    </div>

                    <Link to="/"><span className="text-center">I already have a membership</span></Link>
                </div>

                <Modal
                    isOpen={modalIsOpen}
                    contentLabel="Modal"
                    style={{overlay:{
                        backgroundColor: 'rgba(0, 0, 0, 0.3)'
                    },
                        content : {
                            position: 'relative',
                            top: '0',
                            right: '0',
                            bottom: '0',
                            left: '0',
                            background: '#fff',
                            overflow: 'auto',
                            WebkitOverflowScrolling: 'touch',
                            borderRadius: '4px',
                            outline: 'none',
                            padding: '20px',
                            margin: '20px',
                            marginTop: '40px'
                        }
                    }
                    }
                    onRequestClose={this.closeModal}>
                    <SignupConfirmed closeModal={this.closeModal} />
                </Modal>
            </div>
        );
    }
};

const mapStateToProps = (state) => {
    return {
        isAuthenticated: state.auth.isAuthenticated
    };
}

Signup.contextTypes = {
    router: PropTypes.object.isRequired
}

export default connect(mapStateToProps)(withRouter(Signup));