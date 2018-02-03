import {SET_IS_AUTHENTICATED} from '../reducers/auth/types'
import {REMOVE_USER_INFO} from '../reducers/user/types'
import {backendUrl} from '../conf/application.properties'
import {postAction} from "./serverActions";

export function logout() {
    localStorage.removeItem('authToken');
    fetch(backendUrl+'/auth/logout', {
        method: "get",
        mode: 'cors',
        credentials: 'include'
    });

    return dispatch => {
        dispatch({type:SET_IS_AUTHENTICATED, payload: false});
        dispatch({type:REMOVE_USER_INFO})
    }
}

export function login(data) {

    let requestBody = {
        username : data.username,
        password : data.password
    };

    return dispatch => {
        return postAction(dispatch, '/auth/login', requestBody).then(
            (response) => {
                new Promise((resolve, reject) =>
                    response.json().then(
                        (json) => {
                            if (json.error) {
                                reject(json);
                                return;
                            }
                            localStorage.setItem("authToken", json.token);
                            dispatch({type: SET_IS_AUTHENTICATED, payload: true});
                            resolve();
                        }
                    )
                );
            }
        );
    }
}

