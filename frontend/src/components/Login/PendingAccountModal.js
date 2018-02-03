import React from 'react'
import validator from 'validator'
import {requestAccountConfirmationLink} from '../../utils/AuthUtils'

export default class PendingAccountModal extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            email: "",
            errors: {},
            success: "",
            isLoading: false
        };

        this.onChange = this.onChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.validateInput = this.validateInput.bind(this);
    }

    onChange(e) {
        this.setState({[e.target.name] : e.target.value});
    }

    validateInput(formData) {
        let errors = {};
        if (!validator.isEmail(formData.email)) errors.email = "Not a valid email address";
        this.setState({errors});
        return !errors.email;
    }

    onSubmit(e) {
        e.preventDefault();
        if (this.validateInput(this.state)) {
            this.setState({errors:{}, isLoading:true});
            requestAccountConfirmationLink({...this.state}).then(
                () => {this.setState({success : "Successfully requested a new link, you will receive an email shortly."})},
                () => {this.setState({isLoading: false, errors: {server: 'Something went wrong while requesting a new link, please try again later.'}})}
            );
        }
    }

    render() {

        const {errors, success} = this.state;

        return (
            <form>
                <h4>Your account has not yet been confirmed, please check your email for the confirmation link, or request a new one below.</h4>
                <div className="form-group has-feedback">
                    <input onChange={this.onChange} type="text" className="form-control" placeholder="Email" name="email" />
                    <span className="glyphicon glyphicon-envelope form-control-feedback"></span>
                    {errors && errors.email && <span className="form-input-error-message">{errors.email}</span>}
                </div>
                <div className="row">
                    <div className="col-xs-6"></div>
                    <div className="col-xs-6">
                        <button onClick={this.onSubmit} disabled={this.state.isLoading} className="btn btn-primary btn-block btn-flat">Send link</button>
                    </div>
                    <span>{success}</span>
                </div>
            </form>
        )
    }
}
