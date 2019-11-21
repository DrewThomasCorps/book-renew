import React, {Fragment, useState} from 'react';
import { connect } from 'react-redux';
import {Link, Redirect} from 'react-router-dom';
import PropTypes from 'prop-types';
import { loginUser } from '../../actions/auth';

import logo from "../../resources/book_renew_white_vertical_logo.svg";
import background from "../../resources/bookRenew_pattern.png";
import {setAlert} from "../../actions/alert";

const Login2 = ({setAlert, loginUser, isAuthenticated}) => {
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
                <div className="rstart-1 rspan-2 cstart-1 cspan-12 cspan-md-2 item">
                    <img className="img-responsive" src={logo} alt={"logo"}/>
                </div>
                <div className="rstart-1 rspan-2 cstart-12 cspan-1 item">
                    <button className="btn btn-white">Sign Up</button>
                </div>
                <div className="rstart-3 rspan-3 cstart-1 cspan-12 cstart-md-4 cspan-md-4 item item-js-start hidden-md">
                    <h1><span className="ul">Bo</span>oktrading Platform</h1>
                </div>
                <div className="rstart-4 rspan-5 cstart-1 cspan-12 cspan-md-4 cspan-md-4 item w-100">
                    <form className="form" action="">
                        <input type="email" name="email" placeholder="Email"/>
                            <br/>
                                <input type="password" name="password" placeholder="Password"/>
                                    <br/>
                                        <input className="btn btn-primary d-block m-auto mt-5" type="submit"
                                               value="Sign In"/>
                    </form>
                </div>
                <div className="rstart-1 rspan-12 cstart-md-7 cspan-md-6 cstart-lg-5 cspan-lg-8 br-patterned-bg z-index-back">
                </div>
            </div>
        </Fragment>
    )
};

Login2.propTypes = {
    loginUser: PropTypes.func.isRequired,
    isAuthenticated: PropTypes.bool,
    setAlert: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
    isAuthenticated: state.auth.isAuthenticated
});

export default connect(mapStateToProps, {setAlert,loginUser})(Login2);