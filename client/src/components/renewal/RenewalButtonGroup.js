import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { offerRenewal } from '../../actions/renewal';
import { cancelRenewal } from '../../actions/renewal';
import { completeRenewal } from '../../actions/renewal';
import { acceptRenewal } from '../../actions/renewal';

const RenewalButtonGroup = (
    {
        auth,
        renewal: {trader,tradee},
        renewal,
        offerRenewal,
        cancelRenewal,
        completeRenewal,
        acceptRenewal
   }
   ) => {

    const renderButtons = () => {
        switch(renewal.status) {
            case("active"):
                return getActiveButtons();
            case("pending"):
                return getPendingButtons();
            case("declined"):
                return getDeclinedButtons();
            case("completed"):
                return getCompletedButtons();
            case undefined:
                return getPotentialButtons();
            default:
                return <Fragment/>;
        }
    };

    const getActiveButtons = () => {
        return (
            <Fragment>
                <button onClick={() => completeRenewal(renewal.id)}>
                    Complete
                </button>
                <button onClick={() => cancelRenewal(renewal.id)}>
                    Cancel
                </button>
            </Fragment>
        )
    };

    const getDeclinedButtons = () => {
        return (
            <Fragment>
                <button disabled>Declined</button>
            </Fragment>
        )
    };

    const getPendingButtons = () => {
        let buttonGroup = <Fragment/>;

        if(trader.user.id === auth.user.id){
            buttonGroup = <Fragment>
                <button disabled>
                    Pending
                </button>
            </Fragment>
        } else {
            buttonGroup = <Fragment>
                <button onClick={() => acceptRenewal(renewal.id)}>
                    Accept
                </button>
                <button onClick={() => cancelRenewal(renewal.id)}>
                    Decline
                </button>
            </Fragment>
        }

        return buttonGroup;
    };

    const getCompletedButtons = () => {
        return (
            <Fragment>
                <button disabled>
                    Completed
                </button>
            </Fragment>
        )
    };

    const getPotentialButtons = () => {
        return (
            <Fragment>
                <button onClick={()=> offerRenewal(trader.id,tradee.id)}>Send Offer</button>
            </Fragment>
        )
    };

    return (
        <Fragment>
            {renderButtons()}
        </Fragment>
    )
};

RenewalButtonGroup.propTypes = {
    renewal: PropTypes.object.isRequired,
    offerRenewal: PropTypes.func.isRequired,
    cancelRenewal: PropTypes.func.isRequired,
    completeRenewal: PropTypes.func.isRequired,
    acceptRenewal: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
    auth: state.auth
});

export default connect(mapStateToProps,{offerRenewal,cancelRenewal,completeRenewal,acceptRenewal})(RenewalButtonGroup);