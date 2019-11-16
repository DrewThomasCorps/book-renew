import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

const Header = ({pageTitle}) => {

    return(
        <Fragment>
            <section className={"row"}>
                <section className={"col-12 order-first content-header"}>
                    <h1 className={"float-left text-uppercase font-weight-bolder main-text-color header-border"}>{pageTitle}</h1>
                </section>
            </section>
        </Fragment>
    )
};

Header.propTypes = {
    pageTitle: PropTypes.string.isRequired,
};

export default connect()(Header);