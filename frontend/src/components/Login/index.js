import React from 'react'
import PropTypes from 'prop-types';
import Modal from 'react-modal'
import {connect} from 'react-redux'
import {Link} from 'react-router-dom'
import RequestPasswordReset from './RequestPasswordReset'
import PendingAccountModal from './PendingAccountModal'
import {login} from '../../actions/authActions'

import {websiteTitle} from '../../conf/application.properties'
import withRouter from "react-router-dom/es/withRouter";
import {retrieveUserInfo} from "../../actions/userActions";

import './style.css';

class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            email: '',
            password: '',
            errors: {},
            modalIsOpen: false,
            pendingAccountModalIsOpen: false,
            isLoading: false
        };

        this.onChange = this.onChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.closeModal = this.closeModal.bind(this);
        this.openModal = this.openModal.bind(this);
        this.openPendingAccountModal = this.openPendingAccountModal.bind(this);
        this.closePendingAccountModal = this.closePendingAccountModal.bind(this);
    }

    onChange(e) {
        this.setState({[e.target.name] : e.target.value});
    }

    onSubmit(e) {
        e.preventDefault();
        this.setState({errors:{}, isLoading:true});
        this.props.login({...this.state, username: this.state.email}).then(
            () => {
                this.props.closeModal();
                setTimeout(
                    () => {
                        this.props.retrieveUserInfo();
                    }, 20)
            },
            (json) => {
                if (json.errorCode === 1003) {
                    this.setState({isLoading: false, errors: {}});
                    this.openPendingAccountModal();
                    return;
                } else {
                    this.setState({isLoading: false, errors: {server: json.description}});
                }
            }
        );
    }

    openModal() {
        this.setState({modalIsOpen: true});
    }

    closeModal() {
        this.setState({modalIsOpen: false});
    }

    openPendingAccountModal() {
        this.setState({pendingAccountModalIsOpen: true});
    }

    closePendingAccountModal() {
        this.setState({pendingAccountModalIsOpen: false});
    }

    render() {

        const {errors, modalIsOpen, pendingAccountModalIsOpen} = this.state;

        return (
            <div className="login-box">
                <div className="login-logo">
                    <Link to="/">{websiteTitle}</Link>
                </div>
                <div className="login-box-body">
                    <p className="login-box-msg">Sign in to start your session</p>

                    {
                        errors &&
                        errors.server &&
                            <p className="login-server-error">
                                {errors.server}
                            </p>
                    }

                    <form onSubmit={this.onSubmit} method="post">
                        <div className="form-group has-feedback">
                            <input onChange={this.onChange} type="text" className="form-control" placeholder="Email" name="email" />
                            <span className="glyphicon glyphicon-envelope form-control-feedback"></span>
                            {errors && errors.email && <span className="form-input-error-message">{errors.email}</span>}
                        </div>
                        <div className="form-group has-feedback">
                            <input onChange={this.onChange} type="password" className="form-control" placeholder="Password" name="password" />
                            <span className="glyphicon glyphicon-lock form-control-feedback"></span>
                            {errors && errors.password && <span className="form-input-error-message">{errors.password}</span>}
                        </div>
                        <div className="row">
                            <div className="col-xs-8"></div>
                            <div className="col-xs-4">
                                <button disabled={this.state.isLoading} type="submit" className="btn btn-primary btn-block btn-flat">Sign In</button>
                            </div>
                        </div>
                    </form>

                    <div className="social-auth-links text-center">
                        <p>- OR -</p>
                        <a className="btn btn-block btn-social btn-facebook btn-flat"><i className="fa fa-facebook"></i> Sign in using
                            Facebook</a>
                        <a className="btn btn-block btn-social btn-google btn-flat"><i className="fa fa-google-plus"></i> Sign in using
                            Google+</a>
                    </div>

                    <a onClick={this.openModal}>I forgot my password</a>
                    <br/>
                    <Link to="/signup"><span className="text-center">Register a new membership</span></Link>
                </div>

                <Modal
                        isOpen={modalIsOpen}
                        contentLabel="Modal"
                        ariaHideApp={false}
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
                    <RequestPasswordReset closeModal={this.closeModal} />
                </Modal>

                <Modal
                        isOpen={pendingAccountModalIsOpen}
                        contentLabel="Modal"
                        ariaHideApp={false}
                        style={
                            {overlay:{
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
                        onRequestClose={this.closePendingAccountModal}>
                    <PendingAccountModal closeModal={this.closePendingAccountModal} />
                </Modal>

            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        isAuthenticated: state.auth.isAuthenticated
    };
}

Login.contextTypes = {
    router: PropTypes.object.isRequired
}

export default withRouter(connect(mapStateToProps, {login, retrieveUserInfo})(Login));
