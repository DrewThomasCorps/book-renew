import React, {Fragment, useState} from 'react';
import axios from 'axios';
import {Link} from 'react-router-dom';
import {BASE_URL} from '../../config/config';

const Registration = () => {
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
            const User = {
                username,
                email,
                password
            };

            try {
                const setHeaders = {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                };

                const body = JSON.stringify(User);

                const res = await axios.post(BASE_URL+"/users", body, setHeaders);
                console.log(res);
            } catch (err) {
                console.error(err);
            }
            console.log(User);
        }
    };

    return(
        <Fragment>
            <h1 className={"form-heading"}>Account Creation</h1>
            <form className={"form form-registration"} onSubmit={handleAccountCreationSubmit}>
                <input className={"form-input"} type={"text"} onChange={handleInputChange} name="username" value={username} placeholder={"Username"} />
                <input className={"form-input"} type={"email"} onChange={handleInputChange} name="email" value={email} placeholder={"Email Address"} />
                <input className={"form-input"} type={"password"} onChange={handleInputChange} name="password"  value={password} placeholder={"Password"} />
                <input className={"form-input"} type={"password"} onChange={handleInputChange}
                       name="passwordConfirmation"  value={passwordConfirmation} placeholder={"Password Confirmation"}
                />
                <input className={"btn btn-primary"} type={"submit"} value={"Sign Up"} />
            </form>
            <div className={"info-box"}>
                <p className={"info-box-text"}>Already have an account? <Link className={"info-box-link"} to={"/"}>Sign in here</Link></p>
            </div>
        </Fragment>
    )
};

export default Registration;