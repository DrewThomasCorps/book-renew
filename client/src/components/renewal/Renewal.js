import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { offerRenewal } from '../../actions/renewal';

const Renewal = ({
    renewal:{trader,tradee,status},
    offerRenewal
}) => {

    return (
        <Fragment>
            <article className={"card"}>
                <div className={"card-body"}>
                    <h5 className={"card-title h6 my-1"}>{trader.book.title}</h5>
                    <small>{trader.book.isbn}</small>
                </div>
                <div className={"card-body"}>
                    <h5 className={"card-title h6 my-1"}>{tradee.book.title}</h5>
                    <small>{tradee.book.isbn}</small>
                </div>
                <div className={"card-body"}>
                    { !status ?
                        <button className={"btn btn-primary"} onClick={() => offerRenewal(trader.book_user_id,tradee.book_user_id)}>
                            Send Offer
                        </button> : <Fragment/>
                    }
                </div>
            </article>
        </Fragment>
    )
};

Renewal.propTypes = {
    renewal: PropTypes.object.isRequired,
    offerRenewal: PropTypes.func.isRequired
};

export default connect(null,{offerRenewal})(Renewal);