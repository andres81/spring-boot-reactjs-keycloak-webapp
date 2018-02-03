import {deleteAction, getAction, putAction} from "./serverActions";

export function deleteAccount(userId) {
    return dispatch => {
        return deleteAction(dispatch, '/user/'+userId);
    }
}

export function getAllPermissions() {
    return dispatch => {
        return getAction(dispatch, '/permission');
    }
}

export function getUserPermissions(userId) {
    return dispatch => {
        return getAction(dispatch, '/user/' + userId + '/permissions');
    }
}

export function saveUserPermissions(userId, permissions) {
    return dispatch => {
        return putAction(dispatch, '/user/' + userId + '/permissions', permissions);
    }
}
