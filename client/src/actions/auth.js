import axios from 'axios';
import {
    REGISTER_SUCCESS,
    REGISTER_FAIL,
    LOGIN_SUCCESS,
    LOGIN_FAIL
} from "../actions/types";
import {BASE_URL} from "../config/config";

export const registerUser = ({username,email,password}) => async dispatch => {
  const setHeaders = {
      headers: {
          'Content-Type': 'application/json'
      }
  };

  const body = JSON.stringify({username,email,password});

  try {
      const res = await axios.post(BASE_URL+"/users", body, setHeaders);
      dispatch({
          type: REGISTER_SUCCESS,
          payload: res.data
      })
  } catch (err) {
      console.log(err);
      dispatch({
          type: REGISTER_FAIL
      })
  }
};

export const loginUser = ({email,password}) => async dispatch => {
    const setHeaders = {
        headers: {
            'Content-Type': 'application/json'
        }
    };

    const body = JSON.stringify({email,password});

    try {
        dispatch({
            type: LOGIN_SUCCESS,
            payload: {'authToken' : '123456'}
        })
    } catch (err) {
        console.log(err);
        dispatch({
            type: LOGIN_FAIL
        })
    }
};

