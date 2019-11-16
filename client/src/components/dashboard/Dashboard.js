import React, {Fragment} from 'react';
import Navigation from "../layout/Navigation";
import Header from '../layout/Header';

const Dashboard = () => {
    return (
        <Fragment>
            <Navigation />
            <section className={"row br-content-container bg-grey"}>
                <section className={"col-12 vh-100"}>
                    <Header pageTitle={"Dashboard"} />
                </section>
            </section>
        </Fragment>
    )
};

export default Dashboard;