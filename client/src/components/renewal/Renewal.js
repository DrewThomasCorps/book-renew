import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

const Renewal = ({
     renewal:{book1,book2}
}) => {

    return (
        <Fragment>
            <section className={"col-12"}>
                <h5 className={"card-title h6 my-1"}>{book1}</h5>
                <h5 className={"card-title h6 my-1"}>{book2}</h5>
            </section>
        </Fragment>
    )
};

Renewal.propTypes = {
    renewal: PropTypes.object.isRequired
};

const mapStateToProps = state => ({
})

export default connect(mapStateToProps, null)(Renewal);