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
            <div className={"col-12 container-fluid"}>
                <h2>Add Books</h2>
                <BookForm />
                {/*TODO Map books when GET request completed*/}
                {/*<div className={"row"}>*/}
                {/*    {books.map(book => (*/}
                {/*        <Book key={book.isbn} book={book.title} />*/}
                {/*    ))}*/}
                {/*</div>*/}
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