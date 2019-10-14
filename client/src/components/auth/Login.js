import React, {Fragment, useState} from 'react';
import {Link} from 'react-router-dom';
import axios from "axios";
import {BASE_URL} from "../../config/config";

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

            try {
                const setHeaders = {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                };

                const body = JSON.stringify(signInAttempt);
                //axios request
                if(password === password){
                    localStorage.setItem('isAuthenticated', 'true');
                }

            } catch (err){
                console.log(err);
            }
        }
    };

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

export default Login;