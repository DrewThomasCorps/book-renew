import React, {Fragment, useState} from 'react';
import {Link} from 'react-router-dom';

const Registration = () => {
    const [accountData, setAccountData] = useState({
        email: '',
        password: '',
        passwordConfirmation: ''
    });

    const {email, password, passwordConfirmation} = accountData;

    const handleInputChange = e => setAccountData({ ...accountData, [e.target.name] : e.target.value});

    const handleAccountCreationSubmit = async e => {
        e.preventDefault();

        if(password !== passwordConfirmation){
            console.log("Password mismatch");
        } else {
            const newAccount = {
                email,
                password
            };
            console.log(newAccount);
        }
    };

    return(
        <Fragment>
            <h1>Account Creation</h1>
            <form onSubmit={handleAccountCreationSubmit}>
                <input type={"email"} onChange={handleInputChange} name="email" value={email} placeholder={"Email Address"} />
                <input type={"password"} onChange={handleInputChange} name="password"  value={password} placeholder={"Password"} />
                <input type={"password"} onChange={handleInputChange} name="passwordConfirmation"  value={passwordConfirmation} placeholder={"Password Confirmation"} />
                <input type={"submit"} value={"Sign Up"} />
            </form>
            <div>
                <p>Already have an account? <Link to={"/"}>Sign in here</Link></p>
            </div>
        </Fragment>
    )
};

export default Registration;