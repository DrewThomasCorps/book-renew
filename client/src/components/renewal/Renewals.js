import React, {Fragment, useEffect} from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import Navigation from '../layout/Navigation';
import Renewal from '../renewal/Renewal';
import Header from '../layout/Header';
import { getRenewals } from '../../actions/renewal';


const Renewals = ({auth: {loading},getRenewals,renewal:{renewals}}) => {
    useEffect(()=>{
        getRenewals();
    }, [getRenewals]);

    return (
        <Fragment>
            <Navigation />
            <section className={"row br-content-container"}>
                <Header pageTitle={"Renewals"}/>

                {!loading && (
                    <Fragment>
                        {
                            renewals.map(renewal =>  (
                                <Renewal key={renewal.id} renewal={renewal}/>
                            ))
                        }
                    </Fragment>
                )}
            </section>
        </Fragment>
        )
};

Renewals.propTypes = {
    getRenewals: PropTypes.func.isRequired,
    renewal: PropTypes.object.isRequired,
    auth: PropTypes.object.isRequired
};

const mapStateToProps = state => ({
    renewal: state.renewal,
    auth: state.auth
});

export default connect(mapStateToProps,{ getRenewals })(Renewals);