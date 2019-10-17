import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import Navigation from "../layout/Navigation";

const Dashboard = props => {
    return (
        <Fragment>
            <div>Dashboard</div>
            <Navigation />
        </Fragment>
    )
};

Dashboard.propTypes = {

};

export default Dashboard;