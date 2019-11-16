import React, {Fragment} from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { deleteBook } from '../../actions/book';

const Book = ({
    deleteBook,
    auth,
    book: { id, book }
  }) => {

    return (
        <Fragment>
                <div className={"book-card-container col-12 col-sm-10 col-md-10 col-lg-8 col-xl-6"}>
                    <div className={"card"}>
                        <div className={"card-body book-card-body"}>
                            <h5 className={"card-title h6 my-1"}>{book.title}</h5>
                            <small>{book.isbn}</small>
                            {!auth.loading && auth.isAuthenticated === true && (
                                <button className={"btn btn-primary btn-sm float-right"} onClick={() => deleteBook(id)}>Delete</button>
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