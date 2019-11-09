import React, {Fragment} from 'react';
import Navigation from "../layout/Navigation";
import Books from "../books/Books";

const Dashboard = () => {
    return (
        <Fragment>
            <Navigation />
            <section className={"row br-content-container"}>
                <div className={"col-12 main-text-color"}>
                <h1 className={"float-left text-uppercase font-weight-bolder main-text-color"}>Dashboard</h1>
                <Books />
                </div>
            </section>
        </Fragment>
    )
};

export default Dashboard;