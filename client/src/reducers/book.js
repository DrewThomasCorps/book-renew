import {
    GET_BOOKS,
    BOOK_ERROR
} from '../actions/types';

const initialState = {
    books: [],
    book: null,
    loading: true,
    error: {}
};

export default function(state= initialState, action){
    const {type, payload} = action;

    switch(type) {
        case GET_BOOKS:
            return {
                ...state,
                books: payload,
                loading: false
            }
        case BOOK_ERROR:
            return {
                ...state,
                books: payload,
                loading: false
            }
        default:
            return state;
    }
}