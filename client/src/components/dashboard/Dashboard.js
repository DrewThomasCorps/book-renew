import React, {Fragment} from 'react';
import Navigation from "../layout/Navigation";
import Header from '../layout/Header';

const Dashboard = () => {
    return (
        <Fragment>
            <Navigation />
            <section className={"row br-content-container"}>
                    <Header pageTitle={"Dashboard"} />
            </section>
        </Fragment>
    )
};

export default Dashboard;