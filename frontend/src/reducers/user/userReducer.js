import {REMOVE_USER_INFO, SET_USER_INFO} from './types'

const initialState =  {};

export default (state = initialState, action = {}) => {
    switch(action.type) {
        case SET_USER_INFO:
            return Object.assign({}, state, {
                email: action.payload.email,
                fullName: action.payload.fullName,
                permissions: action.payload.permissions,
                admin: action.payload.admin,
                id: action.payload.id
            });
        case REMOVE_USER_INFO:
            return Object.assign({}, state, {
                email: '',
                fullName: '',
                permissions: '',
                id: '',
                admin: false
            });
        default:
            return state;
    }
}
