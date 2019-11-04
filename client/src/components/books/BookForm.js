import React, {useState}from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { addBook } from '../../actions/book';

const BookForm = ({ addBook }) => {
    const [isbn, setISBN] = useState('');
    const [title, setTitle] = useState('');
    const [bookStatus, setStatus] = useState('owned');

    return (
        <div className={"row book-form-container"}>
            
            <form className="col-10 br-white add-books-margin left-blue-border shadow" onSubmit={e => {
                e.preventDefault();
                addBook({ isbn, title, bookStatus });
                setISBN('');
                setTitle('');
                setStatus('owned');
            }}>
                <h3 className={"text-capitalize grey-text-color add-books-content-margin"}>Add Books</h3>
                <select className="form-control isbn-input-box" value={bookStatus} onChange={e=> setStatus(e.target.value)}>
                    <option value={"owned"}>Library</option>
                    <option value={"wishlist"}>Wishlist</option>
                </select>
                <input type={"text"} className={"isbn-input-box"} placeholder={"Book Title"} value={title} onChange={e => setTitle(e.target.value)}/>
                <input type={"text"} className={"isbn-input-box"} placeholder={"Book ISBN"} value={isbn} onChange={e => setISBN(e.target.value)}/>
                <input type={"submit"} className={"btn btn-dark my-1 md-3 add-book-btn"} value={"Add Book"}/>
            </form>
        </div>
    )
};

BookForm.propTypes = {

};

export default connect(null, { addBook })(BookForm);