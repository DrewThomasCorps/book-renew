import { combineReducers} from "redux";
import auth from './auth';
import book from './book';
import alert from './alert';
import renewal from './renewal';

export default combineReducers({
    auth,
    book,
    alert,
    renewal
});