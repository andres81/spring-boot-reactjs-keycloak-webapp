import {backendUrl} from "../conf/application.properties";
import {logout} from "./authActions";

const AUTHENTICATION_HEADER_NAME="Authorization"

export function deleteAction(dispatch, path) {
    var headers = new Headers();
    headers.set(AUTHENTICATION_HEADER_NAME, localStorage.getItem("authToken"))
    return doServerRequest("DELETE", path, headers, null, dispatch, (resolve, reject, response) => {resolve(response)}, (resolve, reject, response) => {resolve(response)});
};

export function getAction(dispatch, path) {
    var headers = new Headers();
    headers.set(AUTHENTICATION_HEADER_NAME, 'Bearer ' + localStorage.getItem("authToken"))
    return doServerRequest("GET", path, headers, null, dispatch,
        (resolve, reject, response) => {
            response.json().then(
                (json) => {
                    if (json.error) {
                        reject(json);
                        return;
                    }
                    resolve(json);
                }
            )
        },
        (resolve, reject, response) => {resolve(response)});
}

export function putAction(dispatch, path, body) {
    var headers = new Headers();
    headers.set(AUTHENTICATION_HEADER_NAME, localStorage.getItem("authToken"));
    headers.set("Content-Type", "application/json");
    return doServerRequest("PUT", path, headers, JSON.stringify(body), dispatch, (resolve, reject, response) => {resolve(response)}, (resolve, reject, response) => {resolve(response)});
}

export function postAction(dispatch, path, body) {
    var headers = new Headers();
    headers.set(AUTHENTICATION_HEADER_NAME, localStorage.getItem("authToken"));
    headers.set("Content-Type", "application/json");
    return doServerRequest("POST", path, headers, JSON.stringify(body), dispatch, (resolve, reject, response) => {resolve(response)}, (resolve, reject, response) => {resolve(response)});
}

export function doServerRequest(method, path, headers, body, dispatch, do_200, do_204) {
    return fetch(backendUrl+path, {
        method: method,
        mode: 'cors',
        headers: headers,
        credentials: 'include',
        body: body
    }).then(response =>
        new Promise(function (resolve, reject) {
            switch (response.status) {
                case 200:
                    do_200(resolve, reject, response);
                    break;
                case 204:
                    do_204(resolve, reject, response);
                    break;
                case 401:
                    dispatch(logout());
                    break;
                case 403:
                    console.info('You are missing the required access rights... this should never happen!');
                    reject();
                default:
                    console.info('Unknown http code: ', response.status);
                    reject();
            }
        })
    );
}
