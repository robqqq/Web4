export function register(login, password) {
    return getResponse('/users/register', {username: login, password: password, roles: []}, 'POST', false)
}

export function login(login, password) {
    return getResponse('/users/login', {username: login, password: password}, 'POST', false)
}

export function getAll() {
    return getResponse('/points/get-all', {})
}

export function check(point) {
    return getResponse('/points/check', point)
}

export function refresh() {
    //todo
    return fetch('/api/users/refresh', {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        headers: {
            'Content-type': 'application/json',
            'Accept' : 'application/json'
        },
        body: JSON.stringify({refreshToken: sessionStorage.getItem("refreshToken")}) // body data type must match "Content-Type" header
    });
    //return getResponse('/api/users/refresh', {refreshToken: sessionStorage.getItem("refreshToken")})
}

export function clear() {
    return getResponse('/points/clear')
}

function getResponse(url = '', data = null, method='GET', tokenNeeded = true) {
    let token = sessionStorage.getItem("token")
    let httpHeaders;
    if(tokenNeeded && token && token !== 'null'){
        httpHeaders = {
            'Content-type': 'application/json',
            'Accept' : 'application/json',
            'Authorization' : `Bearer ${token}`
        };
    } else {
        httpHeaders = {
            'Content-type': 'application/json',
            'Accept' : 'application/json'
        };
    }
    if(method === 'POST') {
        return fetch(url, {
            method: method, // *GET, POST, PUT, DELETE, etc.
            headers: httpHeaders,
            body: JSON.stringify(data) // body data type must match "Content-Type" header
        });
    } else {
        if(data !== null)
            url += '?' + new URLSearchParams(data).toString();
        return fetch(url, {
            method: method, // *GET, POST, PUT, DELETE, etc.
            headers: httpHeaders
        });
    }

}