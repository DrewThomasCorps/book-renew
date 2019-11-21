import React, {Fragment, useEffect} from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import Navigation from "../layout/Navigation";
import Header from '../layout/Header';
import Renewal from '../renewal/Renewal';
import { getRenewals } from '../../actions/renewal';

const Dashboard = ({auth: {loading},getRenewals,renewal:{renewals}}) => {
    useEffect(()=>{
        getRenewals();
    }, [getRenewals]);

    return (
        <Fragment>
            <Navigation />
            <section className={"row br-content-container"}>
                <Header pageTitle={"Dashboard"} />
                <section className={"col-12"}>
                    {!loading && (
                        <Fragment>
                            {renewals.map(renewal =>  (
                                renewal.status === "active" ? <Renewal key={renewal} renewal={renewal}/> : <Fragment/>
                            ))}
                        </Fragment>
                    )}
                </section>
            </section>
        </Fragment>
    )
};

Dashboard.propTypes = {
    getRenewals: PropTypes.func.isRequired,
    renewal: PropTypes.object.isRequired,
    auth: PropTypes.object.isRequired
};

const mapStateToProps = state => ({
    renewal: state.renewal,
    auth: state.auth
});

export default connect(mapStateToProps,{getRenewals})(Dashboard);