import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { deleteBook } from '../../actions/book';

const Book = ({
    deleteBook,
    auth,
    book: { id, title, isbn }
  }) => {

    return (
        <Fragment>
            <div className={"row"}>
                <div className={"card col-4"}>
                    <div className={"card-body"}>
                        <h5 className={"card-title"}>{title}</h5>
                        <small>{isbn}</small>
                    </div>
                    <div className={"card-body"}>
                        {!auth.loading && auth.isAuthenticated === true && (
                        <a href={"#"} className={"btn btn-primary btn-sm"} onClick={e=> deleteBook(id)}>Delete</a>
                        )}
                    </div>
                </div>
            </div>
        </Fragment>
    )
};

Book.propTypes = {
    book: PropTypes.object.isRequired,
    auth: PropTypes.object.isRequired,
    deleteBook : PropTypes.func.isRequired
};

const mapStateToProps = state => ({
    auth: state.auth
});

export default connect(mapStateToProps, {deleteBook})(Book);