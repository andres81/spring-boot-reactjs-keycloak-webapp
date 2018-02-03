import {combineReducers} from 'redux';

import auth from './auth/authReducer';
import user from './user/userReducer';

export default combineReducers({
    auth,
    user
});
