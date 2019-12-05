import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { offerRenewal } from '../../actions/renewal';
import { updateRenewal } from '../../actions/renewal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCheck, faTimes } from '@fortawesome/free-solid-svg-icons'


const RenewalButtonGroup = (
    {
        auth,
        renewal: {trader,tradee},
        renewal,
        offerRenewal,
        updateRenewal
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
            <div className={"br-vCenter btn-group"}>
                <button className={"btn btn-primary"} onClick={() => updateRenewal(renewal.id,"completed")}>
                    <FontAwesomeIcon icon={faCheck} />
                </button>
                <button className={"btn btn-secondary"} onClick={() => updateRenewal(renewal.id,"declined")}>
                    <FontAwesomeIcon icon={faTimes} />
                </button>
            </div>
        )
    };

    const getDeclinedButtons = () => {
        return (
            <Fragment>
                <button className={"btn btn-status br-vCenter"} disabled>Declined</button>
            </Fragment>
        )
    };

    const getPendingButtons = () => {
        let buttonGroup = <Fragment/>;

        if(trader.user.id === auth.user.id){
            buttonGroup = <Fragment>
                <button className={"btn btn-status br-vCenter"} disabled>
                    Pending
                </button>
            </Fragment>
        } else {
            buttonGroup = <div className={"br-vCenter btn-group"}>
                <button className={"btn btn-primary"} onClick={() => updateRenewal(renewal.id,"active")}>
                    <FontAwesomeIcon icon={faCheck} />
                </button>
                <button className={"btn btn-secondary"} onClick={() => updateRenewal(renewal.id,"declined")}>
                    <FontAwesomeIcon icon={faTimes} />
                </button>
            </div>
        }

        return buttonGroup;
    };

    const getCompletedButtons = () => {
        return (
            <Fragment>
                <button className={"btn btn-status br-vCenter"} disabled>
                    Completed
                </button>
            </Fragment>
        )
    };

    const getPotentialButtons = () => {
        return (
            <Fragment>
                <button className={"btn btn-primary br-vCenter"} onClick={()=> offerRenewal(trader.id,tradee.id)}>Send Offer</button>
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
    updateRenewal: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
    auth: state.auth
});

export default connect(mapStateToProps,{offerRenewal,updateRenewal})(RenewalButtonGroup);