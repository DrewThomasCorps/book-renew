import React, {Fragment, useEffect} from 'react';
import { connect } from 'react-redux';
import { getBooks } from "../../actions/book";
import PropTypes from 'prop-types';
import Book from "./Book";

const Books = ({bookType,getBooks,book:{books,loading}}) => {
    useEffect(() => {
        getBooks();
    }, [getBooks]);

    return loading ? <Fragment/> :
        <Fragment>
            <h3 className={"text-capitalize grey-text-color"}>{bookType}</h3>
                {books.map(book => (
                    book.status === bookType ? <Book key={book.id} book={book} /> : <Fragment/>
                ))}
        </Fragment>
};

Books.propTypes = {
    getBooks: PropTypes.func.isRequired,
    book: PropTypes.object.isRequired,
    bookType: PropTypes.string.isRequired
};

const mapStateToProps = state => ({
    book: state.book
});

export default connect(mapStateToProps,{getBooks})(Books);