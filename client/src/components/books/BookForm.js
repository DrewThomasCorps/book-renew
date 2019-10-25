import React, {useState}from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { addBook } from '../../actions/book';

const BookForm = ({ addBook }) => {
    const [bookISBN, setISBN] = useState('');

    return (
        <div className={"book-form"}>
            <form onSubmit={e => {
                e.preventDefault();
                addBook({ bookISBN });
                setISBN('');
            }}>
                <input type={"text"} className={"isbn-input-box"} placeholder={"Book ISBN"} value={bookISBN} onChange={e => setISBN(e.target.value)}/>
                <input type={"submit"} className={"btn btn-dark my-1 md-3 add-book-btn"} value={"Add Book"}/>
            </form>
        </div>
    )
};

BookForm.propTypes = {

};

export default connect(null, { addBook })(BookForm);