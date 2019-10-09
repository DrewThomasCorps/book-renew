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
            <h1>Sign In</h1>
            <form onSubmit={handleLoginSubmit}>
                <input type={"email"} name={"email"} value={email} onChange={handleInputChange} placeholder={"Email Address"} />
                <input type={"password"} name={"password"} value={password} onChange={handleInputChange}  placeholder={"Password"} />
                <input type={"submit"} value={"Sign In"} />
            </form>
            <div>
                <p>Don't have an account? <Link to={"/registration"}>Sign up here</Link></p>
            </div>
        </Fragment>
    )
};

export default Login;