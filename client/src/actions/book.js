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
                isbn: "0747532745",
                status: "library"

            },{
                id: 6,
                title: "Harry Potter and the Chamber of Secrets",
                isbn: "0747532746",
                status: "wishlist"

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
    const setHeaders = {
        headers: {
            'Content-Type': 'application/json'
        }
    };

    try {
        const res = await axios.post(BASE_URL + "/books/" + formData.bookStatus, formData, setHeaders);
        dispatch({
            type: ADD_BOOK,
            payload: res.book
        })
    } catch (error) {
        dispatch({
            type: BOOK_ERROR,
            payload: { msg: error.response.statusText, status: error.response.status },
            loading: false
        })
    }
};