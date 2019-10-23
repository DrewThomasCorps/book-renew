import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import Navigation from "../layout/Navigation";

const Trades = props => {
    return (
        <Fragment>
            <Navigation />
            <section className={"row br-content-container"}>
                <div className={"col-12"}>
                    <h1>Trades</h1>
                </div>
            </section>
        </Fragment>
    )
};

Trades.propTypes = {

};

export default Trades;