import axios from 'axios';
import {
    GET_BOOKS,
    BOOK_ERROR
} from './types';
import {BASE_URL} from "../config/config";

export const getBooks = () => async dispatch => {
    try {
        // const res = await axios.get(BASE_URL + '/books');
        dispatch({
            type: GET_BOOKS,
            payload: {}
        })
    } catch (err) {
        dispatch({
            type: BOOK_ERROR,
            payload: { msg: err.response.statusText, status: err.response.status }
        });
    }
};