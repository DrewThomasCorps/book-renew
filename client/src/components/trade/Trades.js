import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import Navigation from "../layout/Navigation";

const Trades = props => {
    return (
        <Fragment>
            <Navigation />
            <section className={"row br-content-container"}>
                <div className={"col-12 main-text-color"}>
                    <h1>Trades</h1>
                    <hr/>
                </div>
            </section>
        </Fragment>
    )
};

Trades.propTypes = {

};

export default Trades;