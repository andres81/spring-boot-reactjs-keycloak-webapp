import React from 'react'
import {connect} from 'react-redux'
import validator from 'validator';
import Modal from 'react-modal'

import './profile.css'
import {deleteUser, retrieveUserInfo} from "../../actions/userActions";
import {logout} from "../../actions/authActions";
import {putAction} from "../../actions/serverActions";

class Profile extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            emailValue : '',
            fullNameValue : '',
            passwordValue : '',
            emailEditing : false,
            fullNameEditing : false,
            passwordEditing : false,
            deleteModalIsOpen: false
        };

        this.edit = this.edit.bind(this);
        this.onFieldChange = this.onFieldChange.bind(this);
        this.validateInput = this.validateInput.bind(this);
        this.update = this.update.bind(this);
        this.repasswordChange = this.repasswordChange.bind(this);
        this.ondeleteAccount = this.ondeleteAccount.bind(this);
        this.openDeleteModal = this.openDeleteModal.bind(this);
        this.closeDeleteModal = this.closeDeleteModal.bind(this);
    }

    openDeleteModal() {
        this.setState({deleteModalIsOpen:true});
    }

    closeDeleteModal() {
        this.setState({deleteModalIsOpen:false});
    }

    ondeleteAccount(e) {
        e.preventDefault();
        this.closeDeleteModal();
        this.props.deleteUser().then(
            () => {
                this.props.logout();
            }
        );
    }

    validateInput() {
        let errors = {};
        let state = this.state;
        if (state.fullNameEditing && validator.isEmpty(state.fullNameValue)) errors.fullName = "Enter a name";
        if (state.emailEditing && !validator.isEmail(state.emailValue)) errors.email = "Not a valid email address";
        if (state.passwordEditing && validator.isEmpty(state.passwordValue)) errors.password = "Enter a password";
        if (state.passwordEditing && state.passwordValue !== state.repasswordValue) {
            errors.repassword = "Passwords do not match!";
        }

        this.setState({errors});

        return Object.keys(errors).length === 0;
    }

    update(e) {

        e.preventDefault();
        if(!this.validateInput()) return;

        let newData = {};
        if (this.state.fullNameEditing) newData.fullName = this.state.fullNameValue;
        if (this.state.emailEditing) newData.email = this.state.emailValue;
        if (this.state.passwordEditing) {
            newData.password = this.state.passwordValue;
            newData.repassword = this.state.repasswordValue;
        }

        putAction(this.props.dispatch, '/user', newData).then(
            () => {
                this.props.retrieveUserInfo();
                this.setState({successMessage: true, editing: false});
                setTimeout(function() { this.setState({successMessage: false}); }.bind(this), 2000);
            }
        );
    }

    edit(e) {
        var fieldName = e.currentTarget.dataset['field'];
        var dataField = fieldName+"Editing";
        var object = {};
        var editing = this.state[dataField];
        object[dataField] = !editing;
        var propsValue = this.props.user[fieldName];
        if (editing) {
            object[fieldName+"Value"] = propsValue ? propsValue : "";
            if (fieldName === 'password') object.repasswordValue = "";
        }
        this.setState(object, function() {
            this.validateInput();
            this.setState({editing: this.state.passwordEditing || this.state.emailEditing || this.state.fullNameEditing});
        }.bind(this));
    }

    onFieldChange(e) {
        var dataField = e.currentTarget.dataset['field']+"Value";
        var object = {};
        object[dataField] = e.target.value;
        this.setState(object);
    }

    repasswordChange(e) {
        this.setState({repasswordValue : e.target.value}, function() {
            this.validateInput();
        }.bind(this));
    }

    componentDidMount() {
        var email = this.props.user.email;
        var nickname = this.props.user.fullName;
        if (email && email !== null
                && nickname && nickname !== null) {
            this.setState({
                emailValue: this.props.user.email,
                fullNameValue: this.props.user.fullName
            });
        }
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
            emailValue: nextProps.user.email,
            fullNameValue: nextProps.user.fullName,
            passwordValue: "",
            repasswordValue: "",
            emailEditing: false,
            fullNameEditing: false,
            passwordEditing: false
        });
    }

    render() {

        const {emailValue, fullNameValue, passwordValue, successMessage, editing, errors, emailEditing, fullNameEditing, passwordEditing} = this.state;

        return (
            <div>
                <h3>Profile</h3>
                &nbsp;&nbsp;&nbsp;
                <span className={"green " + (successMessage ? "success-message-visible" : "success-message-fadeout")}>Your profile has been successfully updated</span>
                <div className="profile-input-block">

                    <label className="profile-input-label">Email</label>
                    <br/>
                    <input onChange={this.onFieldChange} onBlur={this.validateInput} className="profile-input" disabled={!emailEditing && "disabled"} value={emailValue} data-field="email"/>
                    <span onClick={this.edit} className="profile-input-control" data-field="email">
                        {emailEditing && <i className="fa fa-times"></i>}
                        {!emailEditing && <i className="fa fa-pencil"></i>}
                    </span>
                    {errors && errors['email'] && <p className="profile-validation-error-message">{errors['email']}</p>}

                    <label className="profile-input-label">Nickname</label>
                    <br/>
                    <input onChange={this.onFieldChange} onBlur={this.validateInput} className="profile-input" disabled={!fullNameEditing && "disabled"} value={fullNameValue} data-field="fullName"/>
                    <span onClick={this.edit} className="profile-input-control" data-field="fullName">
                        {fullNameEditing && <i className="fa fa-times"></i>}
                        {!fullNameEditing && <i className="fa fa-pencil"></i>}
                    </span>
                    {errors && errors['fullName'] && <p className="profile-validation-error-message">{errors['fullName']}</p>}

                    <label className="profile-input-label">Password</label>
                    <br/>
                    <input onChange={this.onFieldChange} onBlur={this.validateInput} className="profile-input" disabled={!passwordEditing && "disabled"} value={passwordValue} data-field="password" type='password'/>
                    <span onClick={this.edit} className="profile-input-control" data-field="password">
                        {passwordEditing && <i className="fa fa-times"></i>}
                        {!passwordEditing && <i className="fa fa-pencil"></i>}
                    </span>
                    {errors && errors['password'] && <p className="profile-validation-error-message">{errors['password']}</p>}

                    {passwordEditing &&
                        <div>
                            <label className="profile-input-label">Password Repeat</label>
                            <br/>
                            <input onBlur={this.validateInput} onChange={this.repasswordChange} type="password" className="profile-input" value={this.state.repasswordValue}/>
                            {errors && errors.repassword && <p className="profile-validation-error-message">{errors.repassword}</p>}
                        </div>
                    }
                </div>
                <div>
                    <span onClick={this.openDeleteModal} className="profile-delete-button">Delete account</span>
                    <button disabled={!editing} onClick={this.update} type="submit" className="btn btn-info pull-right">Update</button>
                </div>
                <Modal
                    isOpen={this.state.deleteModalIsOpen}
                    contentLabel="Modal"
                    ariaHideApp={false}
                    style={{
                        overlay:{
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
                            marginLeft: 'auto',
                            marginRight: 'auto',
                            width:'500px',
                            maxWidth: '95%',
                            marginTop: '40px'
                        }
                    }}
                    onRequestClose={this.closeDeleteModal}>
                    <h1>Account Deletion</h1>
                    <p>You are about to delete your account. This operation is permanent. Are you sure you want to delete your account?</p>
                    <button onClick={this.closeDeleteModal} className="btn pull-right">Cancel</button>
                    <button onClick={this.ondeleteAccount} style={{marginRight: '20px'}} className="btn btn-danger pull-right">Delete</button>
                </Modal>
            </div>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        user: state.user
    };
}

export default connect(mapStateToProps, {retrieveUserInfo, logout, deleteUser})(Profile);