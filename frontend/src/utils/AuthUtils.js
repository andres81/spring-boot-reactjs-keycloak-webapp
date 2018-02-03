import {backendUrl} from '../conf/application.properties'

export function requestPasswordReset(data) {
    return fetch(backendUrl+'/auth/resetpassword?email='+data.email,
        {
            method: "PUT"
        })
        .then(function(response) {
            return new Promise(function(resolve, reject) {
                switch (response.status) {
                    case 204:
                        resolve();
                        break;
                    default:
                        reject(response);
                }
            })
        });
}

export function requestAccountConfirmationLink(data) {
    return fetch(backendUrl+'/auth/userVerificationToken?email='+data.email, {method: "PUT"})
        .then(function(response) {
            return new Promise(function(resolve, reject) {
                switch (response.status) {
                    case 204:
                        resolve();
                        break;
                    default:
                        reject(response);
                }
            })
        });
}

export function signup(data) {

    var myHeaders = new Headers();
    myHeaders.set("Content-Type", "application/json")
    return fetch(backendUrl+'/auth/signup', {
        method: "POST",
        mode: 'cors',
        headers: myHeaders,
        body: JSON.stringify(data)
    }).then(function(response) {
        return new Promise(function(resolve, reject) {
            if (response.status === 204) resolve();
            else if (response.status === 200) {
                response.json().then(function(json) {
                    reject(json);
                });
            }
            else reject();
        })
    });
}

export function resetPassword(data) {

    let requestBody = {
        newPassword : data.newPassword,
        resetToken : data.token
    };

    var myHeaders = new Headers();
    myHeaders.set("Content-Type", "application/json")
    return fetch(backendUrl+'/auth/resetpassword', {
        method: "POST",
        mode: 'cors',
        headers: myHeaders,
        body: JSON.stringify(requestBody)
    }).then(function(response) {
        return new Promise(function(resolve, reject) {
            switch (response.status) {
                case 204:
                    resolve();
                    break;
                default:
                    reject(response);
            }
        })
    });
}
