import React, {Fragment, useState} from 'react';

const Registration = () => {
    const [accountData, setAccountData] = useState({
        email: '',
        password: '',
        passwordConfirmation: ''
    });

    const {email, password, passwordConfirmation} = accountData;

    const handleFormSubmit = async e => {
        e.preventDefault();

        if(password !== passwordConfirmation){
            console.log("Password mismatch");
        } else {
            const newAccount = {
                email,
                password
            };
            console.log(newAccount)
        }
    };

    const handleInputChange = e => setAccountData({ ...accountData, [e.target.name] : e.target.value});

    return(
        <Fragment>
            <h1>Account Creation</h1>
            <form onSubmit={handleFormSubmit}>
                <input type={"email"} onChange={handleInputChange} name="email" value={email} placeholder={"Email Address"} />
                <input type={"password"} onChange={handleInputChange} name="password"  value={password} placeholder={"Password"} />
                <input type={"password"} onChange={handleInputChange} name="passwordConfirmation"  value={passwordConfirmation} placeholder={"Password Confirmation"} />
                <input type={"submit"} value={"Sign Up"} />
            </form>
        </Fragment>
    )
};

export default Registration;