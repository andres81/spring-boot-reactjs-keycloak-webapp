import React from 'react'
import validator from 'validator'
import {requestPasswordReset} from '../../utils/AuthUtils'

export default class RequestPasswordReset extends React.Component {

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
            requestPasswordReset({...this.state}).then(
                () => {this.setState({success : "Successfully reset your password, you will receive an email shortly."})},
                () => {this.setState({isLoading: false, errors: {server: 'Something went wrong while resetting your password, please try again!'}});}
            );
        }
    }

    render() {

        const {errors, success} = this.state;

        return (
            <form onSubmit={this.onSubmit} method="post">
                <div className="form-group has-feedback">
                    <input onChange={this.onChange} type="text" className="form-control" placeholder="Email" name="email" />
                    <span className="glyphicon glyphicon-envelope form-control-feedback"></span>
                    {errors && errors.email && <span className="form-input-error-message">{errors.email}</span>}
                </div>
                <div className="row">
                    <div className="col-xs-6"></div>
                    <div className="col-xs-6">
                        <button disabled={this.state.isLoading} type="submit" className="btn btn-primary btn-block btn-flat">Reset Password</button>
                    </div>
                    <span>{success}</span>
                </div>
            </form>
        )
    }
}








