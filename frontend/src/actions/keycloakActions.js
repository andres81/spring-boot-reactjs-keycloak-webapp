import {SET_AUTHENTICATED} from "../reducers/keycloak/types";

export function setAuthenticated(isAuthenticated) {
    return dispatch => {
        dispatch({type:SET_AUTHENTICATED, payload: isAuthenticated})
    }
}