import {
    REGISTER_SUCCESS,
    REGISTER_FAIL
} from '../actions/types';

const initialState = {
    authToken: localStorage.getItem('authToken'),
    isAuthenticated: null,
    loading: true,
    user: null
};

export default function(state = initialState, action){
    const { type, payload} = action;

    switch(type){
        case REGISTER_SUCCESS:
            localStorage.setItem('authToken', payload.authToken);
            return {
                ...state,
                ...payload,
                isAuthenticated: true,
                loading: false
            };
        case REGISTER_FAIL:
            localStorage.removeItem('authToken');
            return {
                ...state,
                token: null,
                isAuthenticated: false,
                loading: false
            };
        default:
            return state;
    }
};