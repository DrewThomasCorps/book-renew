import React, {Fragment, useState} from 'react';
import { connect } from 'react-redux';
import {Link, Redirect} from 'react-router-dom';
import PropTypes from 'prop-types';
import { registerUser } from '../../actions/auth';
import { setAlert } from '../../actions/alert';
import logo from "../../resources/book_renew_white_vertical_logo.svg";

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
            <section className={"row"}>
                <div className={"col-12 br-container-fluid bg-blue"}>
                    <section className={"br-vCenter"}>
                        <form className={"form form-registration text-center"} onSubmit={handleAccountCreationSubmit}>
                            <img src={logo} alt={"book renew logo"} className={"my-5"}/>
                            <input className={"form-input mb-4 form-control"} type={"text"} onChange={handleInputChange} name={"name"} value={name} placeholder={"Name"} />
                            <input className={"form-input mb-4 form-control"} type={"email"} onChange={handleInputChange} name="email" value={email} placeholder={"Email Address"} />
                            <input className={"form-input mb-4 form-control"} type={"password"} onChange={handleInputChange} name="password"  value={password} placeholder={"Password"} />
                            <input className={"form-input mb-4 form-control"} type={"password"} onChange={handleInputChange}
                                   name="passwordConfirmation"  value={passwordConfirmation} placeholder={"Password Confirmation"}/>
                            <input className={"btn btn-primary mb-4 btn-lg btn-block"} type={"submit"} value={"Sign Up"} />
                            <div className={"info-box mb-5"}>
                                <p className={"info-box-text"}>Already have an account? <Link className={"info-box-link text-white font-weight-bold"} to={"/"}>Sign in</Link> here.</p>
                            </div>
                        </form>
                    </section>
                </div>
            </section>
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