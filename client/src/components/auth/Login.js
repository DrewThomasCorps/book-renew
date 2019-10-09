import React, {Fragment} from 'react';
import {Link} from 'react-router-dom';

const Login = () => {
    return(
        <Fragment>
            <h1>Sign In</h1>
            <form>
                <input type={"email"} placeholder={"Email Address"} />
                <input type={"password"} placeholder={"Password"} />
                <input type={"submit"} value={"Sign In"} />
            </form>
            <div>
                <p>Don't have an account? <Link to={"/registration"}>Sign up here</Link></p>
            </div>
        </Fragment>
    )
};

export default Login;