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
            <div className={"col-6 col-md-4 col-lg-2 my-2"}>
                <div className={"card"}>
                    <img className={"card-img-top"} alt={"Book Cover"} src={"https://via.placeholder.com/150"} />
                        <div className={"card-body"}>
                            <h5 className={"card-title"}>{title}</h5>
                            <small>{isbn}</small>
                        </div>
                        <div className={"card-body"}>
                            {!auth.loading && auth.isAuthenticated === true && (
                            <a href={"#"} className={"btn btn-primary"} onClick={e=> deleteBook(id)}>Delete</a>
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