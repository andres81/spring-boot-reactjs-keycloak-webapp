import React from 'react'

import Modal from 'react-modal'

import './style.css'
import CollapsableContainer from "../../CollapsableContainer";
import {logout} from "../../../actions/authActions";
import {connect} from "react-redux";
import withRouter from "react-router-dom/es/withRouter";
import {MODAL_DELETE_USER} from "./styles";
import {getAction} from "../../../actions/serverActions";
import PermissionModal from "./PermissionModal";
import {deleteAccount} from "../../../actions/adminActions";

class UserList extends React.Component {

    constructor(props) {
        super(props)

        this.retrieveUserList = this.retrieveUserList.bind(this);
        this.closeDeleteModal = this.closeDeleteModal.bind(this);
        this.deleteAccount = this.deleteAccount.bind(this);
        this.openDeleteModal = this.openDeleteModal.bind(this);
        this.permissionIconClicked = this.permissionIconClicked.bind(this);
        this.closePermissionModal = this.closePermissionModal.bind(this);

        this.state = {
            deleteModalIsOpen: false
            ,permissionModalActive: false
        };
    }

    openDeleteModal(user) {
        this.setState({
            deleteModalIsOpen: true
            ,userToDelete: user
        });
    }

    closeDeleteModal() {
        this.setState({
            deleteModalIsOpen: false
            ,userToDelete: null
        });
    }

    deleteAccount(userId) {
        this.closeDeleteModal();
        this.props.deleteAccount(userId).then(
            () => this.retrieveUserList()
        );
    }

    permissionIconClicked(user) {
        this.setState({
            permissionModalActive: true
            ,userForModal: user
        });
    }

    closePermissionModal() {
        this.setState({
            permissionModalActive: false
        })
    }

    componentWillMount() {
        this.retrieveUserList();
    }

    retrieveUserList() {
        getAction(this.props.dispatch, '/user/list')
            .then(json => {
                this.setState({
                    users: json
                })
            });
    }

    render() {

        const user = this.props.user;
        const canEditPermissions = user.admin || (user.permissions && user.permissions.includes('user_permissions'));

        let users = this.state.users;
        let userRows = [];
        if (users && users !== null && users.length !== 0) {
             userRows = users.map(
                user =>
                    <tr key={user.id}>
                        <td>{user.fullName}</td>
                        <td>{user.email}</td>
                        <td>{user.admin ? 'Yes' : 'No'}</td>
                        <td>{!user.admin && canEditPermissions && <i onClick={e=>this.permissionIconClicked(user)} className="fa fa-key permission-icon"></i>}</td>
                        <td>{!(user.admin || this.props.user.id === user.id) && <i onClick={e=>this.openDeleteModal(user)} className="fa fa-times remove-icon"></i>}</td>
                    </tr>
            );
        }

        return (
            <CollapsableContainer headerTitle='Users'>
                <div className="container">
                    <div className="row">
                        <table className="table">
                            <thead>
                            <tr>
                                <th scope="col">Fullname</th>
                                <th scope="col">Email</th>
                                <th scope="col">Is admin</th>
                                <th scope="col"></th>
                            </tr>
                            </thead>
                            <tbody>
                            {userRows}
                            </tbody>
                        </table>
                    </div>
                </div>
                {this.state.permissionModalActive && <PermissionModal user={this.state.userForModal} requestClose={this.closePermissionModal} /> }
                <Modal
                    isOpen={this.state.deleteModalIsOpen}
                    contentLabel="Modal"
                    ariaHideApp={false}
                    style={MODAL_DELETE_USER}
                    onRequestClose={this.closeDeleteModal}>
                    <h1>Delete user</h1>
                    <p>You are about to delete user "{this.state.userToDelete && this.state.userToDelete.email}". This operation is permanent. Are you sure you want to delete this user?</p>
                    <button onClick={this.closeDeleteModal} className="btn pull-right">Cancel</button>
                    <button onClick={e => this.deleteAccount(this.state.userToDelete.id)} style={{marginRight: '20px'}} className="btn btn-danger pull-right">Delete</button>
                </Modal>
            </CollapsableContainer>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        user: state.user
    };
}

export default withRouter(connect(mapStateToProps, {logout, deleteAccount})(UserList));
