import axios from 'axios';
import {
    GET_POTENTIAL_RENEWALS,
    GET_RENEWALS,
    RENEWAL_ERROR,
    OFFER_RENEWAL,
    CANCEL_RENEWAL
} from "./types";
import {BASE_URL} from "../config/config";

export const getPotentialRenewals = () => async dispatch => {
    try {
        const res = await axios.get(BASE_URL + "/users/potential-trades");
        dispatch({
            type: GET_POTENTIAL_RENEWALS,
            payload: res.data
        })

    } catch (error) {
        dispatch({
            type: RENEWAL_ERROR,
            loading: false
        })
    }
};

export const getRenewals = () => async dispatch => {
    try {
        const res = await axios.get(BASE_URL + "/users/renewals");
        dispatch({
            type: GET_RENEWALS,
            payload: res.data
        })

    } catch (error) {
        dispatch({
            type: RENEWAL_ERROR,
            loading: false
        })
    }
};

export const offerRenewal = (trader_book_user_id,tradee_book_user_id) => async dispatch => {
    const setHeaders = {
        headers: {
            'Content-Type' : 'application/json'
        }
    };

    const body = JSON.stringify(
        {
        "trader_book_user_id" : trader_book_user_id,
        "tradee_book_user_id" : tradee_book_user_id
    });

    try {
        await axios.post(BASE_URL + "/users/renewals", body, setHeaders);
        dispatch({
            type: OFFER_RENEWAL
        });
    } catch (error) {
        dispatch({
            type: RENEWAL_ERROR,
            payload: error,
            loading: false
        })
    }
};

export const cancelRenewal = id => async dispatch => {
    //TODO Handle PUT axios request for cancelRenewal
    try {
        dispatch({
            type: CANCEL_RENEWAL,
            payload: id
        })
    } catch (error) {
        dispatch({
            type: RENEWAL_ERROR,
            payload: error
        })
    }
};

