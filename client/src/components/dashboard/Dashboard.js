import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import Navigation from "../layout/Navigation";
import Books from "../books/Books";

const Dashboard = props => {
    return (
        <Fragment>
            <Navigation />
            <section className={"row br-content-container"}>
                <div className={"col-12 main-text-color"}>
                <h1>Dashboard</h1>
                <hr/>
                <Books />
                </div>
            </section>
        </Fragment>
    )
};

Dashboard.propTypes = {

};

export default Dashboard;