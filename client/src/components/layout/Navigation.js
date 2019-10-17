import React, {Fragment} from 'react';
import {Link} from 'react-router-dom';

const Navigation = () => {
    return(
        <Fragment>
            <nav className={"br-navbar"}>
                <ul className={"br-navbar-nav"}>
                    <li className={"br-nav-item"}>
                        <Link to={"/dashboard"}>Dashboard</Link>
                    </li>
                    <li className={"br-nav-item"}>
                        <Link to={"/profile"}>Profile</Link>
                    </li>
                </ul>
            </nav>
        </Fragment>
    )
};

export default Navigation;