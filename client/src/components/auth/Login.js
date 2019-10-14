import React, {Fragment, useState} from 'react';
import { connect } from 'react-redux';
import {Link, Redirect} from 'react-router-dom';
import PropTypes from 'prop-types';
import { loginUser } from '../../actions/auth';

const Login = ({loginUser, isAuthenticated}) => {
    const [loginInformation, setLoginInformation] = useState({
        email: '',
        password: ''
    });

    const {email, password} = loginInformation;

    const handleInputChange = e => setLoginInformation({ ...loginInformation, [e.target.name] : e.target.value});

    const handleLoginSubmit = async e => {
        e.preventDefault();
        loginUser(email,password);
    };

    if(isAuthenticated){
        return <Redirect to={"/dashboard"}/>;
    }

    return(
        <Fragment>
            <h1 className={"form-heading"}>Sign In</h1>
            <form className={"form form-login"} onSubmit={handleLoginSubmit}>
                <input className={"form-input"} type={"email"} name={"email"} value={email} onChange={handleInputChange} placeholder={"Email Address"} />
                <input className={"form-input"} type={"password"} name={"password"} value={password} onChange={handleInputChange}  placeholder={"Password"} />
                <input className={"btn btn-primary"} type={"submit"} value={"Sign In"} />
            </form>
            <div className={"info-box"}>
                <p className={"info-box-text"}>Don't have an account? <Link className={"info-box-link"} to={"/registration"}>Sign up here</Link></p>
            </div>
        </Fragment>
    )
};

Login.propTypes = {
    loginUser: PropTypes.func.isRequired,
    isAuthenticated: PropTypes.bool
};

const mapStateToProps = state => ({
    isAuthenticated: state.auth.isAuthenticated
});

export default connect(mapStateToProps, {loginUser})(Login);