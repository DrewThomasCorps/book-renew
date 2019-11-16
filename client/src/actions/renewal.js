import axios from 'axios';
import {
    GET_RENEWALS, RENEWAL_ERROR
} from "./types";

export const getRenewals = () => async dispatch => {
    try {
        const renewals = [
            {
                "id": 1,
                "book1" : "Book 1",
                "book2" : "Book 2"
            },
            {
                "id": 2,
                "book1" : "Book 3",
                "book2" : "Book 4"
            }
        ];
        dispatch({
            type: GET_RENEWALS,
            payload: renewals
        })

    } catch (error) {
        dispatch({
            type: RENEWAL_ERROR
        })
    }
};