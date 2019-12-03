import React, {Fragment, useEffect} from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import Navigation from '../layout/Navigation';
import Header from '../layout/Header';
import Renewal from '../renewal/Renewal';
import { getPotentialRenewals } from '../../actions/renewal';
import { getRenewals } from '../../actions/renewal';

const Renewals = ({auth:{loading,user},getPotentialRenewals,getRenewals,renewal:{renewals,potentialTrades}}) => {
    useEffect(()=>{
        getPotentialRenewals();
        getRenewals();
    }, [getRenewals,getPotentialRenewals]);

    return (
        <Fragment>
            <Navigation />
            <section className={"row br-content-container"}>
                <Header pageTitle={"Renewals"}/>
                <section className={"col-12"}>
                    <h2>Pending</h2>
                {!loading && (
                    <div className={"col-12 col-lg-6 col-xl-3"}>
                        {renewals.map((renewal, index) =>  (
                            renewal.status !== "active" ? <Renewal key={index} renewal={renewal} userId={user.id}/> : <Fragment/>
                        ))}
                    </div>
                )}
                </section>
                <section className={"col-12"}>
                    <h2>Potential</h2>
                {!loading && (
                    <Fragment>
                        {potentialTrades.map((trade, index) =>  (
                            <Renewal key={index} renewal={trade}/>
                        ))}
                    </Fragment>
                )}
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