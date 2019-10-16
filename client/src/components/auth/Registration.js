import React, {Fragment, useState} from 'react';
import { connect } from 'react-redux';
import {Link, Redirect} from 'react-router-dom';
import PropTypes from 'prop-types';
import { registerUser } from '../../actions/auth';

const Registration = ({registerUser, isAuthenticated}) => {
    const [accountData, setAccountData] = useState({
        username: '',
        email: '',
        password: '',
        passwordConfirmation: ''
    });

    const {username, email, password, passwordConfirmation} = accountData;

    const handleInputChange = e => setAccountData({ ...accountData, [e.target.name] : e.target.value});

    const handleAccountCreationSubmit = async e => {
        e.preventDefault();

        if(password !== passwordConfirmation){
            console.log("Password mismatch");
        } else {
            registerUser({username,email,password});
        }
    };

    if(isAuthenticated){
        return <Redirect to={"/dashboard"}/>;
    }

    return(
        <Fragment>
            <form className={"form form-registration text-center"} onSubmit={handleAccountCreationSubmit}>
                <h1 className={"h3 form-heading mb-3"}>Account Creation</h1>
                <input className={"form-input mb-4 form-control"} type={"text"} onChange={handleInputChange} name="username" value={username} placeholder={"Name"} />
                <input className={"form-input mb-4 form-control"} type={"email"} onChange={handleInputChange} name="email" value={email} placeholder={"Email Address"} />
                <input className={"form-input mb-4 form-control"} type={"password"} onChange={handleInputChange} name="password"  value={password} placeholder={"Password"} />
                <input className={"form-input mb-4 form-control"} type={"password"} onChange={handleInputChange}
                       name="passwordConfirmation"  value={passwordConfirmation} placeholder={"Password Confirmation"}/>
                <input className={"btn btn-primary mb-4 btn-lg btn-block"} type={"submit"} value={"Sign Up"} />
                <div className={"info-box"}>
                    <p className={"info-box-text"}>Already have an account? <Link className={"info-box-link text-white"} to={"/"}>Sign in</Link> here.</p>
                </div>
            </form>
        </Fragment>
    )
};

Registration.propTypes = {
    registerUser: PropTypes.func.isRequired,
    isAuthenticated: PropTypes.bool
};

const mapStateToProps = state => ({
    isAuthenticated: state.auth.isAuthenticated
});

export default connect(mapStateToProps, {registerUser})(Registration);