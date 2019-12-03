import React, {Fragment, useEffect} from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import Navigation from '../layout/Navigation';
import Header from '../layout/Header';
import Renewal from '../renewal/Renewal';
import { getPotentialRenewals } from '../../actions/renewal';
import { getRenewals } from '../../actions/renewal';

const Renewals = ({auth:{loading},getPotentialRenewals,getRenewals,renewal:{renewals,potentialTrades}}) => {
    useEffect(()=>{
        getPotentialRenewals();
        getRenewals();
    }, [getRenewals,getPotentialRenewals]);

    return (
        <Fragment>
            <Navigation />
            <section className={"row br-content-container"}>
                <Header pageTitle={"Renewals"}/>
                <section className={"col-12 content-panel"}>
                <section className={"row p-2"}>
                    <div className={"col-12 book-container bg-white left-blue-border shadow"}>
                        <h2>Potential</h2>
                        {!loading && (
                            <Fragment>
                                {potentialTrades.map((trade, index) =>  (
                                    <Renewal key={index} renewal={trade}/>
                                ))}
                            </Fragment>
                        )}
                    </div>
                </section>
                <section className={"row p-2"}>
                    <div className={"col-12 book-container bg-white left-blue-border shadow"}>
                        <h2>Pending</h2>
                        {!loading && (
                            <Fragment>
                                {renewals.map((renewal, index) =>  (
                                    renewal.status === "pending" ? <Renewal key={index} renewal={renewal}/> : <Fragment />
                                ))}
                            </Fragment>
                        )}
                    </div>
                </section>
                <section className={"row p-2"}>
                    <div className={"col-12 book-container bg-white left-blue-border shadow"}>
                        <h2>Complete</h2>
                        {!loading && (
                            <Fragment>
                                {renewals.map((renewal, index) =>  (
                                    renewal.status === "completed" ? <Renewal key={index} renewal={renewal}/> : <Fragment />
                                ))}
                            </Fragment>
                        )}
                    </div>
                </section>
                <section className={"row p-2"}>
                    <div className={"col-12 book-container bg-white left-blue-border shadow"}>
                        <h2>Declined</h2>
                        {!loading && (
                            <Fragment>
                                {renewals.map((renewal, index) =>  (
                                    renewal.status === "declined" ? <Renewal key={index} renewal={renewal}/> : <Fragment />
                                ))}
                            </Fragment>
                        )}
                    </div>
                </section>
              </section>
            </section>
        </Fragment>
        )
};

Renewals.propTypes = {
    getPotentialRenewals: PropTypes.func.isRequired,
    getRenewals: PropTypes.func.isRequired,
    renewal: PropTypes.object.isRequired,
    auth: PropTypes.object.isRequired
};

const mapStateToProps = state => ({
    renewal: state.renewal,
    auth: state.auth
});

export default connect(mapStateToProps,{ getPotentialRenewals,getRenewals })(Renewals);