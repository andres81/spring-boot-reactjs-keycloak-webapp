import React from 'react'
import Modal from 'react-modal'
import Profile from '../Profile'
import {withRouter} from "react-router-dom";
import {connect} from "react-redux";

import Login from "../Login";

class UserProfileModal extends React.Component {

    constructor(props) {
        super(props);
        this.state={modalIsOpen:false};
        this.openModal = this.openModal.bind(this);
        this.closeModal = this.closeModal.bind(this);
    }

    openModal() {
        this.setState({modalIsOpen: true});
    }

    closeModal() {
        this.setState({modalIsOpen:false});
    }

    render() {
        let modalContent = '';
        if (this.props.isAuthenticated) {
            modalContent = <Profile />
        } else {
            modalContent = <Login closeModal={this.closeModal}/>
        }

        return (
            <div>
                <i onClick={this.openModal} className="green fa fa-user"></i>
                <Modal
                        isOpen={this.state.modalIsOpen}
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
                        onRequestClose={this.closeModal}>
                    {modalContent}
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

export default connect(mapStateToProps, {})(withRouter(UserProfileModal));