import {
    GET_POTENTIAL_RENEWALS,
    GET_RENEWALS,
    RENEWAL_ERROR,
    OFFER_RENEWAL,
    CANCEL_RENEWAL,
    COMPLETE_RENEWAL
} from "../actions/types";

const initialState = {
    renewals: [],
    potentialTrades: [],
    loading: true,
    error: {}
};

export default function(state = initialState, action) {
    const {type, payload} = action;

    switch(type){
        case GET_POTENTIAL_RENEWALS:
            return {
                ...state,
                potentialTrades: payload,
                loading: false
            };
        case GET_RENEWALS:
            return {
                ...state,
                renewals: payload,
                loading: false
            };
        case COMPLETE_RENEWAL:
        case CANCEL_RENEWAL:
        case OFFER_RENEWAL:
            return {
                ...state,
                loading: false
            };
        case RENEWAL_ERROR:
            return {
                ...state,
                error: payload
            };
        default:
            return state;
    }
}