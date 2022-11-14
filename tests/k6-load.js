import {URLSearchParams} from "https://jslib.k6.io/url/1.0.0/index.js";
import http from "k6/http";
import {check} from "k6";

let data

export const options = {
    stages: [
        {duration: "20s", target: 20},
        {duration: "30s", target: 50},
        {duration: "1m30s", target: 100},
        {duration: "20s", target: 20},
    ],
};

export function setup() {
    const body = new URLSearchParams({
        "scope": "openid",
        "grant_type": "password",
        "username": `${__ENV.USERNAME}`,
        "password": `${__ENV.PASSWORD}`,
        "client_id": `${__ENV.CLIENT_ID}`,
        "client_secret": `${__ENV.CLIENT_SECRET}`
    })
    const params = {
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    }

    const url = "https://romanowalex.eu.auth0.com/oauth/token"
    const result = http.post(url, body.toString(), params)
    return result.json().access_token
}

export default function (token) {
    const params = {headers: {"Authorization": `Bearer ${token}`}}
    const response = http.get("http://localhost:8480/api/v1/store/orders", params);

    check(response, {
        "status is 200": (r) => r.status === 200,
        "content is present": (r) => !!r.body,
    });
};