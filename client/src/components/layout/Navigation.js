import React, {Fragment} from 'react';
import {Link} from 'react-router-dom';
import exchangeIcon from "../../exchange-icon.svg";
import dashboardIcon from "../../dashboard-icon.svg";
import profileIcon from "../../profile-icon.svg";

const Navigation = () => {
    return(
        <Fragment>
            <nav className={"br-navbar text-center"}>
                <ul className={"br-navbar-nav"}>
                    <li className={"br-nav-item"}>
                        <Link className={"br-nav-link"} to={"/dashboard"}><img alt={"Dashboard Icon"} className={"br-nav-icon"} src={dashboardIcon} /></Link>
                    </li>
                    <li className={"br-nav-item"}>
                        <Link className={"br-nav-link"} to={"/trades"}><img alt={"Trades Icon"} className={"br-nav-icon"} src={exchangeIcon} /></Link>
                    </li>
                    <li className={"br-nav-item"}>
                        <Link className={"br-nav-link"} to={"/profile"}><img alt={"Profile Icon"} className={"br-nav-icon"} src={profileIcon} /></Link>
                    </li>
                </ul>
            </nav>
        </Fragment>
    )
};

export default Navigation;