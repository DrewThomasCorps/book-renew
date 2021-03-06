import axios from 'axios';
import {
    REGISTER_FAIL,
    USER_LOADED,
    AUTH_ERROR,
    LOGIN_SUCCESS,
    LOGIN_FAIL,
    LOGOUT
} from "../actions/types";
import {BASE_URL} from "../config/config";
import setAuthToken from "../utils/setAuthToken";
import {setAlert} from "./alert";

export const loadUser = () => async dispatch => {
    if (localStorage.authToken) {
        setAuthToken(localStorage.authToken)
    }
    axios.get(BASE_URL + "/users/self").then(response => {
        dispatch({
            type: USER_LOADED,
            payload: response.data
        })
    }).catch(error => {
        console.log(error);
        dispatch({
            type: AUTH_ERROR
        })
    });
};

export const registerUser = ({name, email, password}) => async dispatch => {
    const setHeaders = {
        headers: {
            'Content-Type': 'application/json'
        }
    };

    const body = JSON.stringify({name, email, password});

    try {
        await axios.post(BASE_URL + "/users/register", body, setHeaders);

        dispatch(
            loginUser(email, password)
        );
    } catch (error) {
        dispatch(setAlert(error.response.data.message,'danger'));
        dispatch({
            type: REGISTER_FAIL
        })
    }
};

export const loginUser = (email, password) => async dispatch => {
    const setHeaders = {
        headers: {
            'Content-Type': 'application/json'
        }
    };

    const body = JSON.stringify({email, password});

    axios.post(BASE_URL + "/login", body, setHeaders)
        .then(response => {
            dispatch({
                type: LOGIN_SUCCESS,
                payload: {'authToken': response.headers['authorization']}
            });
            dispatch(loadUser());
        }).catch(error => {
            let message;

            switch(error.response.status){
                case(403):
                    message = "Login failed: Incorrect Email or Password";
                    break;
                case(500):
                    message = "Uh oh! Something went wrong. If this happens again, call Drew.";
                    break;
                default:
                    message = "Uh oh! An error has occurred";
            }

            dispatch(setAlert(message,'danger'));
            dispatch({
                type: LOGIN_FAIL
            })
    });
};

export const logoutUser = () => async dispatch => {
    dispatch({
        type: LOGOUT
    });
};

