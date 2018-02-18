import {SHOW_PROFILE} from "../reducers/profile/types";

export function doShowProfile(doShow) {
    return dispatch => {
        dispatch({type:SHOW_PROFILE, payload: doShow})
    }
}