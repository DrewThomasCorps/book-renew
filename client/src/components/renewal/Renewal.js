import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import RenewalButtonGroup from "./RenewalButtonGroup";

const Renewal = ({
    renewal:{trader,tradee,status},
    renewal
}) => {

    return (
        <Fragment>
            <article className={"card"}>
                <div className={"card-body"}>
                    <small>
                        { status === "active" ? tradee.user.email : "Library"}
                    </small>
                    <h5 className={"card-title h6 my-1"}>{trader.book.title}</h5>
                    <small>{trader.book.isbn}</small>
                </div>
                <div className={"card-body"}>
                    <small>
                        { status === "active" ? trader.user.email : "Wishlist"}
                    </small>
                    <h5 className={"card-title h6 my-1"}>{tradee.book.title}</h5>
                    <small>{tradee.book.isbn}</small>
                </div>
                <div className={"card-body"}>
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