import React, {Fragment, useState} from 'react';
import { connect } from 'react-redux';
import {Link, Redirect} from 'react-router-dom';
import PropTypes from 'prop-types';
import { loginUser } from '../../actions/auth';

import logo from "../../resources/book_renew_white_vertical_logo.svg";
import background from "../../resources/bookRenew_pattern.png";
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
            <section className={"row"}>
                <div className={"col-12 br-container-fluid bg-blue"}>
                    <section className={"br-vCenter"}>
                        <form className={"form form-login text-center"} onSubmit={handleLoginSubmit}>
                            <img src={logo} alt={"book renew logo"} className={"my-5"}/>
                            <input className={"form-user-input mb-4 form-control"} type={"email"} name={"email"} value={email} onChange={handleInputChange} placeholder={"Email Address"}/>
                            <input className={"form-user-input mb-4 form-control"} type={"password"} name={"password"} value={password} onChange={handleInputChange} placeholder={"Password"}/>
                            <input className={"btn btn-primary mb-4 btn-lg btn-block"} type={"submit"} value={"Log In"} />
                            <div className={"info-box mb-5"}>
                                <p className={"info-box-text"}>Don't have an account? <Link className={"info-box-link text-white font-weight-bold"} to={"/registration"}>Sign up</Link> here.</p>
                            </div>
                        </form>
                    </section>
                </div>
            </section>
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