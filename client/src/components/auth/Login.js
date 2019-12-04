import React, {Fragment, useState} from 'react';
import { connect } from 'react-redux';
import {Link, Redirect} from 'react-router-dom';
import PropTypes from 'prop-types';
import { loginUser } from '../../actions/auth';

import logo from "../../resources/book_renew_charcoal_horizontal_logo.svg";
import {setAlert} from "../../actions/alert";

const Login = ({setAlert, loginUser, isAuthenticated}) => {
    const [loginInformation, setLoginInformation] = useState({
        email: '',
        password: ''
    });

    const {email, password} = loginInformation;

    const handleInputChange = e => setLoginInformation({ ...loginInformation, [e.target.name] : e.target.value});

    const handleLoginSubmit = async e => {
        e.preventDefault();

        if(email === ''){
            setAlert("Please enter your Email Address", "danger");
        } else if(password === ''){
            setAlert("Oops! You didn't enter your password", "danger");
        }
        else {
            loginUser(email,password);
        }
    };

    if(isAuthenticated){
        return <Redirect to={"/dashboard"}/>;
    }

    return(
        <Fragment>
            <div className="grid-container grid-12 vh-100">
                <div className="rstart-1 rspan-2 cstart-1 cspan-12 cspan-lg-3 cspan-xl-2 item">
                    <img className="img-responsive p-2" src={logo} alt={"logo"}/>
                </div>
                <div className="rstart-1 rspan-2 cstart-11 cspan-2 item hidden-md">
                    <Link className="btn btn-white" to={"/registration"}>Sign Up</Link>
                </div>
                <div className="jumbo-heading rstart-3 rspan-3 cstart-1 cspan-12 cstart-md-1 cspan-md-12 cstart-lg-4 cspan-lg-4 item item-js-start hidden-md">
                    <h1><span className="ul">Bo</span>oktrading Platform</h1>
                </div>
                <div className="rstart-4 rspan-5 rstart-lg-5 cstart-1 cspan-12 cstart-md-3 cspan-md-8 cstart-lg-1 cspan-lg-4 item w-100">
                    <form className="form" onSubmit={handleLoginSubmit}>
                        <input type={"email"} name={"email"} value={email} onChange={handleInputChange} placeholder={"Email"}/>
                        <br/>
                        <input type={"password"} name={"password"} value={password} onChange={handleInputChange} placeholder={"Password"}/>
                        <br/>
                        <input className="btn btn-primary d-block mx-auto mt-5" type="submit" value="Sign In"/>
                    </form>
                    <div className={"text-center"}>
                        Don't have an account? <Link className={"sign-up-link"} to={"/registration"}>Sign Up </Link>here.
                    </div>
                </div>
                <div className="rstart-1 rspan-12 cstart-1 cspan-12 bg-white z-index-back">
                </div>
                <div className="rstart-1 rspan-12 cstart-md-7 cspan-md-6 cstart-lg-5 cspan-lg-8 br-patterned-bg z-index-back hidden-md">
                </div>
            </div>
        </Fragment>
    )
};

Login.propTypes = {
    loginUser: PropTypes.func.isRequired,
    isAuthenticated: PropTypes.bool,
    setAlert: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
    isAuthenticated: state.auth.isAuthenticated
});

export default connect(mapStateToProps, {setAlert,loginUser})(Login);