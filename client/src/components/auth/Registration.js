import React, {Fragment, useState} from 'react';
import { connect } from 'react-redux';
import {Link, Redirect} from 'react-router-dom';
import PropTypes from 'prop-types';
import { setAlert } from '../../actions/alert';
import logo from "../../resources/book_renew_charcoal_horizontal_logo.svg";
import {registerUser} from "../../actions/auth";

const Registration = ({setAlert, registerUser, isAuthenticated}) => {
    const [accountData, setAccountData] = useState({
        name: '',
        email: '',
        password: '',
        passwordConfirmation: ''
    });

    const {name, email, password, passwordConfirmation} = accountData;

    const handleInputChange = e => setAccountData({ ...accountData, [e.target.name] : e.target.value});

    const handleAccountCreationSubmit = async e => {
        e.preventDefault();

        if(name === ''){
            setAlert("Please enter your name", "danger");
        } else if(email === ''){
            setAlert("Please enter a valid Email Address", "danger");
        } else if(password === ''){
            setAlert("Please enter a password", "danger");
        } else if(password !== passwordConfirmation){
            setAlert("Oops! Your passwords don't match", "danger");
        }
        else {
            registerUser({name,email,password});
        }
    };

    if(isAuthenticated){
        return <Redirect to={"/dashboard"}/>;
    }

    return(
        <Fragment>
            <div className="grid-container grid-12 vh-100">
                <div className="rstart-1 rspan-2 cstart-1 cspan-12 cspan-lg-2 item">
                    <img className="img-responsive" src={logo} alt={"logo"}/>
                </div>
                <div className="rstart-1 rspan-2 cstart-12 cspan-1 item hidden-md">
                    <Link className="btn btn-white" to={"/"}>Sign In</Link>
                </div>
                <div className="jumbo-heading rstart-3 rspan-3 cstart-1 cspan-12 cstart-md-1 cspan-md-12 cstart-lg-4 cspan-lg-4 item item-js-start hidden-md">
                    <h1><span className="ul">Bo</span>oktrading Platform</h1>
                </div>
                <div className="rstart-4 rspan-5 rstart-lg-5 cstart-1 cspan-12 cstart-md-3 cspan-md-8 cstart-lg-1 cspan-lg-4 item w-100">
                    <form className="form" onSubmit={handleAccountCreationSubmit}>
                        <input type={"text"} name={"name"} value={name} onChange={handleInputChange} placeholder={"Name"}/>
                        <br />
                        <input type={"email"} name={"email"} value={email} onChange={handleInputChange} placeholder={"Email"}/>
                        <br />
                        <input type={"password"} name={"password"} value={password} onChange={handleInputChange} placeholder={"Password"}/>
                        <br />
                        <input type={"password"} name={"passwordConfirmation"} value={passwordConfirmation} onChange={handleInputChange} placeholder={"Confirm Password"}/>
                        <br />
                        <input className="btn btn-primary d-block mx-auto mt-5" type="submit" value="Sign Up"/>
                    </form>
                    <div className={"text-center"}>
                        Already have an account? <Link to={"/"}>Sign In here.</Link>
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

Registration.propTypes = {
    registerUser: PropTypes.func.isRequired,
    isAuthenticated: PropTypes.bool,
    setAlert: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
    isAuthenticated: state.auth.isAuthenticated
});

export default connect(mapStateToProps, {setAlert,registerUser})(Registration);