import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import Navigation from "../layout/Navigation";

const Dashboard = props => {
    return (
        <Fragment>
            <Navigation />
            <section className={"pl-54 row"}>
                <h1>Dashboard</h1>
            </section>
        </Fragment>
    )
};

Dashboard.propTypes = {

};

export default Dashboard;