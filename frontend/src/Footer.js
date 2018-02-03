import React from 'react';

export default class Footer extends React.Component {
    render() {
        return (
            <footer>
                <div className="container">
                    <div className="row">
                        <div className="col-md-12" style={{textAlign:"center"}}>
                            <span className="copyright">Copyright &copy; Andr&eacute; Schepers 2018</span>
                        </div>
                    </div>
                </div>
            </footer>
        )
    }
}