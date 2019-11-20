import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

const Renewal = ({
     renewal:{trader,tradee}
}) => {

    return (
        <Fragment>
            <article className={"col-12"}>
                <div className={"row"}>
                    <div className={"card col-5"}>
                        <div className={"card-body"}>
                            <h5 className={"card-title h6 my-1"}>{trader.book.title}</h5>
                            <small>{trader.book.isbn}</small>
                        </div>
                    </div>
                    <div className={"card col-5"}>
                        <div className={"card-body"}>
                            <h5 className={"card-title h6 my-1"}>{tradee.book.title}</h5>
                            <small>{tradee.book.isbn}</small>
                        </div>
                    </div>
                </div>
            </article>
        </Fragment>
    )
};

Renewal.propTypes = {
    renewal: PropTypes.object.isRequired
};

const mapStateToProps = state => ({
})

export default connect(mapStateToProps, null)(Renewal);