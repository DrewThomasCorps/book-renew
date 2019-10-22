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
            <article className={"container-fluid book-container py-3"}>
                <div className={"row book"}>
                    <div className={"col-12 col-sm-2 book-cover"}>
                        <img className={"img-fluid"} alt={"Book Cover"} src={"https://via.placeholder.com/150"} />
                    </div>
                    <div className={"col-12 col-sm-9 book-title"}>
                        <h3>{title}</h3>
                        <small>{isbn}</small>
                    </div>
                    {/*Should use user === auth.user._id, requires response data from backend*/}
                    {!auth.loading && auth.isAuthenticated === true && (
                        <div className={"col-12 col-sm-1"}>
                            <button className={"btn btn-danger"} onClick={e=> deleteBook(id)}>Delete</button>
                        </div>
                    )}
                </div>


            </article>
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