import React, {Fragment} from 'react';
import Navigation from "../layout/Navigation";

const Renewals = () => {
    return (
        <Fragment>
            <Navigation />
            <section className={"row br-content-container"}>
                <div className={"col-12 main-text-color"}>
                    <h1 className={"float-left text-uppercase font-weight-bolder main-text-color"}>Renewals</h1>
                </div>
            </section>
        </Fragment>
    )
};

export default Renewals;