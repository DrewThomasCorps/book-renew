import React, {Fragment, useEffect} from 'react';
import { connect } from 'react-redux';
import { getBooks } from "../../actions/book";
import PropTypes from 'prop-types';
import Book from "./Book";
import BookForm from "./BookForm";

const Books = ({getBooks,book:{books,loading}}) => {
    useEffect(() => {
        getBooks();
    }, [getBooks]);

    return loading ? <Fragment /> :
        <Fragment>
            <div className={"col-12"}>
                <h2>Your Books</h2>
                {books.map(book => (
                    <Book key={book.id} book={book} />
                ))}
                <BookForm />
                <hr/>
            </div>
        </Fragment>;
};

Books.propTypes = {
    getBooks: PropTypes.func.isRequired,
    book: PropTypes.object.isRequired
};

const mapStateToProps = state => ({
    book: state.book
});

export default connect(mapStateToProps,{getBooks})(Books);