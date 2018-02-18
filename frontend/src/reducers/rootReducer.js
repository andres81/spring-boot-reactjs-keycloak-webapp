import {combineReducers} from 'redux';
import keycloak from "./keycloak/index";
import profile from "./profile/index";

export default combineReducers({
    keycloak,
    profile
});
