import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import RenewalButtonGroup from "./RenewalButtonGroup";
import renewalArrows from "../../resources/renewal_arrows_primary.svg";

const Renewal = ({
    renewal:{trader,tradee,status},
    renewal
}) => {

    return (
        <Fragment>
            <article className={"row"}>
                <div className={"col-3 book-card-body book-card-container"}>
                    <small>
                        { status === "active" ? trader.user.email : trader.user.name}
                    </small>
                    <h5 className={"card-title h6 my-1"}>{trader.book.title}</h5>
                    <small>{trader.book.isbn}</small>
                </div>
                <div className={"col-3"}>
                    <img alt={"Renewal Arrows"} className={"br-vCenter"} src={renewalArrows} />
                </div>
                <div className={"col-3 book-card-body book-card-container"}>
                    <small>
                        { status === "active" ? tradee.user.email : tradee.user.name}
                    </small>
                    <h5 className={"card-title h6 my-1"}>{tradee.book.title}</h5>
                    <small>{tradee.book.isbn}</small>
                </div>
                <div className={"col-3"}>
                    <RenewalButtonGroup renewal={renewal}/>
                </div>
            </article>
        </Fragment>
    )
};

Renewal.propTypes = {
    renewal: PropTypes.object.isRequired
};

export default connect()(Renewal);