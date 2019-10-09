import React, {Fragment} from 'react';

const Registration = () => {
    return(
        <Fragment>
            <h1>Account Creation</h1>
            <form>
                <input type={"email"} placeholder={"Email Address"} />
                <input type={"password"} placeholder={"Password"} />
                <input type={"password"} placeholder={"Password Confirmation"} />
                <input type={"submit"} value={"Sign Up"} />
            </form>
        </Fragment>
    )
};

export default Registration;