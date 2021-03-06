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
        const res = await axios.get(BASE_URL + "/books");
        dispatch({
            type: GET_BOOKS,
            payload: res.data
        })
    } catch (err) {
        dispatch({
            type: BOOK_ERROR,
            payload: {err}
        });
    }
};

export const deleteBook = id => async dispatch => {
    try {
        await axios.delete(BASE_URL + "/books/" + id);
        dispatch({
            type: DELETE_BOOK,
            payload: id
        });

        console.log('Book removed');
    } catch (err) {
        dispatch({
            type: BOOK_ERROR,
            payload: err
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
            payload: res.data
        })
    } catch (error) {
        dispatch({
            type: BOOK_ERROR,
            payload: error,
            loading: false
        })
    }
};