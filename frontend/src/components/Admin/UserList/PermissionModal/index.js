import React from 'react'

import Modal from 'react-modal'
import {MODAL_STYLE} from "./styles";

import './style.css'
import {connect} from "react-redux";
import {getAllPermissions, getUserPermissions, saveUserPermissions} from "../../../../actions/adminActions";


class PermissionModal extends React.Component {

    constructor(props) {
        super(props);
        this.closeModal = this.closeModal.bind(this);
        this.saveUserPermissions = this.saveUserPermissions.bind(this);
        this.updateUserPermission = this.updateUserPermission.bind(this);

        this.state = {
            userPermissions: {}
        };
    }

    closeModal() {
        this.props.requestClose();
    }

    componentWillMount() {
        this.retrieveUserPermissions();
    }

    retrieveUserPermissions() {

        this.props.getAllPermissions().then(json => {
            this.setState({
                permissions: json
            })
        });

        this.props.getUserPermissions(this.props.user.id)
            .then(json => {
                if (!Array.isArray(json)) {
                    return;
                }
                let userPermissions = {};
                json.forEach((permission) => {
                    userPermissions[permission.name] = true;
                });
                this.setState({
                    userPermissions: userPermissions
                })
            });
    }

    saveUserPermissions() {
        this.props.saveUserPermissions(this.props.user.id, this.state.userPermissions);
    }

    updateUserPermission(e, permission) {
        let currentPermissions = this.state.userPermissions;
        currentPermissions[permission.name] = !(!!currentPermissions[permission.name]);
        this.setState({userPermissions : currentPermissions});
    }

    render() {
        const {user} = this.props;
        let permissionRows = [];
        let permissions = this.state.permissions;
        if (permissions && permissions !== null && permissions.length !== 0) {
            permissions.sort((permission1, permission2) =>
                permission1.name === permission2.name ? 0 : (permission1.name < permission2.name ? -1 : 1)
            );
            permissionRows = permissions.map(
                permission =>
                    <tr key={permission.name}>
                        <td><input onChange={e => this.updateUserPermission(e, permission)} checked={this.state.userPermissions && this.state.userPermissions[permission.name] ? true : false} type='checkbox'/></td>
                        <td>{permission.name}</td>
                        <td>{permission.description}</td>
                    </tr>
            );
        }

        return (
            <Modal
                isOpen={true}
                contentLabel="Modal"
                ariaHideApp={false}
                style={MODAL_STYLE}
                onRequestClose={this.closeDeleteModal}>
                <i onClick={this.closeModal} className="fa fa-times close-icon"></i>
                <h1 className='title'>Permissions</h1>
                <h2 className='title-user'>{user.email}</h2>
                <br/>
                <div style={{textAlign:'center'}}>
                    <button onClick={() => this.saveUserPermissions()} type="button" className="btn btn-success">Save Permissions</button>
                </div>
                <br/><br/>
                <div className="container">
                    <div className="row">
                        <table className="table">
                            <thead>
                            <tr>
                                <th scope="col"></th>
                                <th scope="col">Name</th>
                                <th scope="col">Description</th>
                            </tr>
                            </thead>
                            <tbody>
                                {permissionRows}
                            </tbody>
                        </table>
                    </div>
                </div>
            </Modal>
        )
    }
}

const mapStateToProps = () => {
    return {};
}

export default connect(mapStateToProps, {getAllPermissions, getUserPermissions, saveUserPermissions})(PermissionModal);
