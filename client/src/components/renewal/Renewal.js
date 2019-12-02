import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { offerRenewal } from '../../actions/renewal';

const Renewal = ({
    userId,
    renewal:{trader,tradee,status},
    offerRenewal
}) => {

    let buttonGroup = <Fragment/>;

    switch(status){
        case("active"):
            buttonGroup =
                <Fragment>
                    <button>
                        Complete
                    </button>
                    <button>
                        Cancel
                    </button>
                </Fragment>;
            break;
        case("pending"):
            if(tradee.id === userId){
                buttonGroup = <button disabled>Pending</button>;
            } else {
                buttonGroup =
                    <Fragment>
                        <button>
                            Accept
                        </button>
                        <button>
                            Decline
                        </button>
                    </Fragment>;
            }
            break;
        default:
            buttonGroup = <button onClick={() => offerRenewal(trader.id,tradee.id)}>Send Offer</button>
    }

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
                    { buttonGroup }
                </div>
            </article>
        </Fragment>
    )
};

Renewal.propTypes = {
    userId: PropTypes.number,
    renewal: PropTypes.object.isRequired,
    offerRenewal: PropTypes.func.isRequired
};

export default connect(null,{offerRenewal})(Renewal);