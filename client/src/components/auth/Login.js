import React, {Fragment} from 'react';

const Login = () => {
    return(
        <Fragment>
            <h1>Sign In</h1>
            <form>
                <input type={"email"} placeholder={"Email Address"} />
                <input type={"password"} placeholder={"Password"} />
                <input type={"submit"} value={"Sign In"} />
            </form>
        </Fragment>
    )
};

export default Login;