import React from "react";
import Modal from 'react-modal'

export default function (ModalComponent, requestClose) {
    class ModalWrapper extends React.Component {
        render() {
            return (
                <Modal
                    isOpen={true}
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
                            marginLeft: 'auto',
                            marginRight: 'auto',
                            marginTop: '40px',
                            maxWidth: '500px'
                        }
                    }}
                    onRequestClose={requestClose}>
                    <ModalComponent />
                </Modal>
            );
        }
    }
    return <ModalWrapper />;
}
