import axios from 'axios';
import {BASE_URL} from "../config/config";
import {
    GET_BOOKS,
    BOOK_ERROR,
    DELETE_BOOK,
    ADD_BOOK
} from './types';

export const getBooks = () => async dispatch => {
    try {
        //TODO implement axios request to get books
        const books = [
            {
                id: 5,
                title: "Harry Potter and the Philosopher's Stone",
                isbn: "0747532745"

            }
        ];
        dispatch({
            type: GET_BOOKS,
            payload: books
        })
    } catch (err) {
        dispatch({
            type: BOOK_ERROR,
            payload: { msg: err.response.statusText, status: err.response.status }
        });
    }
};

export const deleteBook = id => async dispatch => {
    try {
        //TODO implement axios request to delete book
        dispatch({
            type: DELETE_BOOK,
            payload: id
        });

        console.log('Book removed');
    } catch (err) {
        dispatch({
            type: BOOK_ERROR,
            payload: { msg: err.response.statusText, status: err.response.status },
            loading: false
        });
    }
};

export const addBook = formData => async dispatch => {
    const config = {
        headers: {
            'Content-Type' : 'application/json'
        }
    };

    try {
        //TODO implement axios request to add book, should return book as JSON
        console.log(formData);
        dispatch({
            type: ADD_BOOK,
            payload:             {
                id: 6,
                title: "Harry Potter and the Chamber of Secrets",
                isbn: formData.bookISBN

            }
        });

        console.log('Book removed');
    } catch (err) {
        dispatch({
            type: BOOK_ERROR,
            payload: { msg: err.response.statusText, status: err.response.status },
            loading: false
        });
    }
};