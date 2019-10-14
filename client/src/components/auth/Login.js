import React, {Fragment, useState} from 'react';
import {Link} from 'react-router-dom';

const Login = () => {
    const [loginInformation, setLoginInformation] = useState({
        email: '',
        password: ''
    });

    const {email, password} = loginInformation;

    const handleInputChange = e => setLoginInformation({ ...loginInformation, [e.target.name] : e.target.value});

    const handleLoginSubmit = async e => {
        e.preventDefault();

        if(email === '' || password === ''){
            console.log("Please enter your email and password");
        } else {
            const signInAttempt = {
                email,
                password
            };
            console.log(signInAttempt);
        }
    };

    return(
        <Fragment>
            <h1 className={"form-heading"}>book renew</h1>
            <form className={"form form-login"} onSubmit={handleLoginSubmit}>
                <h3 className={"form-input"}>Email:</h3>
                <input className={"form-user-input"} type={"email"} name={"email"} value={email} onChange={handleInputChange} placeholder={"Email Address"}/>
                <h3 className={"form-input"}>Password:</h3>
                <input className={"form-user-input"} type={"password"} name={"password"} value={password} onChange={handleInputChange} placeholder={"Password"}/>
                <input className={"btn btn-primary"} type={"submit"} value={"Log In"} />
            </form>
            <div className={"info-box"}>
                <p className={"info-box-text"}>Don't have an account? Click to <Link className={"info-box-link"} to={"/registration"}>Sign up</Link> today.</p>
            </div>
        </Fragment>
    )
};

export default Login;