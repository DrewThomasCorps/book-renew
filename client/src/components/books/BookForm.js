import React, {useState}from 'react';
import { connect } from 'react-redux';
import { addBook } from '../../actions/book';
import { setAlert } from '../../actions/alert';
import PropTypes from 'prop-types';

const BookForm = ({ setAlert, addBook }) => {
    const [isbn, setISBN] = useState('');
    const [title, setTitle] = useState('');
    const [bookStatus, setStatus] = useState('library');


    return (
        <div className={"row p-2"}>
            <form className="col-12 book-form-container bg-white left-blue-border shadow" onSubmit={e => {
                e.preventDefault();

                if(title === ''){
                    setAlert("Please enter a Book Title", "danger");
                } else if(isbn === '' || (isbn.length !== 13 && isbn.length !== 10)){
                    setAlert("Please enter a valid ISBN", "danger");
                } else {
                    addBook({ isbn, title, bookStatus });
                    setISBN('');
                    setTitle('');
                    setStatus('library');
                }

            }}>
                <h3 className={"text-capitalize grey-text-color"}>Add Books</h3>
                <select className="form-control isbn-input-box" value={bookStatus} onChange={e=> setStatus(e.target.value)}>
                    <option value={"library"}>Library</option>
                    <option value={"wishlist"}>Wishlist</option>
                </select>
                <input type={"text"} className={"isbn-input-box"} placeholder={"Book Title"} value={title} onChange={e => setTitle(e.target.value)}/>
                <input type={"text"} className={"isbn-input-box"} placeholder={"Book ISBN"} value={isbn} onChange={e => setISBN(e.target.value)}/>
                <input type={"submit"} className={"btn btn-primary btn-md d-block"} value={"Add Book"}/>
            </form>
        </div>
    )
};

BookForm.propTypes = {
    setAlert: PropTypes.func.isRequired
};

export default connect(null, { setAlert, addBook })(BookForm);