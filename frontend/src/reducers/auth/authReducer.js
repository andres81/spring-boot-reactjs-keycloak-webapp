import {SET_IS_AUTHENTICATED} from './types'

const initialState =  {
    isAuthenticated: localStorage.authToken ? true : false,
};

export default (state = initialState, action = {}) => {
    switch(action.type) {
        case SET_IS_AUTHENTICATED:
            return Object.assign({}, state, {
                isAuthenticated: action.payload
            });
        default:
            return state;
    }
}
