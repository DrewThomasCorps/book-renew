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

    let buttonGroup = <Fragment/>;

    switch(renewal.status){
        case("active"):
            buttonGroup =
                <Fragment>
                    <button onClick={() => completeRenewal(renewal.id)}>
                        Complete
                    </button>
                    <button onClick={() => cancelRenewal(renewal.id)}>
                        Cancel
                    </button>
                </Fragment>;
            break;
        case("pending"):
            if(trader.user.id === auth.user.id){
                buttonGroup = <button disabled>Pending...</button>;
            } else {
                buttonGroup =
                    <Fragment>
                        <button onClick={() => acceptRenewal(renewal.id)}>
                            Accept
                        </button>
                        <button onClick={() => cancelRenewal(renewal.id)}>
                            Decline
                        </button>
                    </Fragment>;
            }
            break;
        case("declined"):
            buttonGroup = <button disabled>Declined</button>;
            break;
        case undefined:
            buttonGroup = <button onClick={() => offerRenewal(trader.id,tradee.id)}>Send Offer</button>;
            break;
        default:
            break;
    }


    return (
        <Fragment>
            {buttonGroup}
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