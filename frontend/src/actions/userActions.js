import {SET_USER_INFO} from '../reducers/user/types'
import {deleteAction, getAction} from "./serverActions";

export function retrieveUserInfo() {
    return dispatch => {
        getAction(dispatch, '/user').then(
            (json) => dispatch({type:SET_USER_INFO, payload: json})
        );
    }
}

export function deleteUser() {
    return dispatch => {
        return deleteAction(dispatch, '/user');
    }
}
