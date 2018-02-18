import {backendUrl} from "../conf/application.properties";

export function deleteAction(dispatch, path, bearerToken) {
    return doServerRequest("DELETE", path, null, bearerToken);
};

export function getAction(path, bearerToken) {
    return doServerRequest("GET", path, null, bearerToken);
}

export function putAction(path, body, bearerToken) {
    return doServerRequest("PUT", path, JSON.stringify(body), bearerToken);
}

export function postAction(dispatch, path, body, bearerToken) {
    return doServerRequest("POST", path, JSON.stringify(body), bearerToken);
}

export function doServerRequest(method, path, body, bearerToken) {
    var headers = new Headers();
    headers.set('Authorization', 'Bearer ' + bearerToken)
    headers.set("Content-Type", "application/json");
    return fetch(backendUrl+path, {
        method: method,
        mode: 'cors',
        headers: headers,
        credentials: 'include',
        body: body
    }).then(response =>
        new Promise(function (resolve) {
            resolve(response);
        })
    );
}
