import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import { logoutUser } from '../../actions/auth';
import { connect } from 'react-redux';

const Header = ({ pageTitle, logoutUser}) => {

    return(
        <Fragment>
            <section className={"col-8 order-first content-header"}>
                <h1 className={"text-uppercase font-weight-bolder main-text-color header-border header-size"}>{pageTitle}</h1>
            </section>
            { pageTitle === "Profile" && (
                <section className={"col-4"}>
                    <button className={"btn btn-dark my-1 md-3 btn-secondary float-right"} onClick={logoutUser}>Log Out</button>
                </section>
            )}
        </Fragment>
    )
};

Header.propTypes = {
    logoutUser: PropTypes.func.isRequired,
    pageTitle: PropTypes.string.isRequired
};

export default connect(null,{logoutUser})(Header);