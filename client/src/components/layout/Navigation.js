import React, {Fragment} from 'react';
import {NavLink} from 'react-router-dom';
import exchangeIcon from "../../resources/exchange-icon.svg";
import dashboardIcon from "../../resources/dashboard-icon.svg";
import profileIcon from "../../resources/profile-icon.svg";

const Navigation = () => {

    return(
        <Fragment>
            <nav className={"br-navbar text-center"}>
                <ul className={"br-navbar-nav"}>
                    <li className={"br-nav-item"}>
                        <NavLink className={"br-nav-link"} activeClassName={"active"} to={"/dashboard"}><img alt={"Dashboard Icon"} className={"br-nav-icon br-vCenter"} src={dashboardIcon} /></NavLink>
                    </li>
                    <li className={"br-nav-item"}>
                        <NavLink className={"br-nav-link"} activeClassName={"active"} to={"/renewals"}><img alt={"Trades Icon"} className={"br-nav-icon br-vCenter"} src={exchangeIcon} /></NavLink>
                    </li>
                    <li className={"br-nav-item"}>
                        <NavLink className={"br-nav-link"} activeClassName={"active"} to={"/profile"}><img alt={"Profile Icon"} className={"br-nav-icon br-vCenter"} src={profileIcon} /></NavLink>
                    </li>
                </ul>
            </nav>
        </Fragment>
    )
};

export default Navigation;