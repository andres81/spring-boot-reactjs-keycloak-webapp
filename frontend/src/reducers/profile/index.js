import {SHOW_PROFILE} from './types'

const initialState =  {showProfile:false};

export default (state = initialState, action = {}) => {
    switch(action.type) {
        case SHOW_PROFILE:
            return Object.assign({}, state, {
                showProfile: action.payload
            });
        default:
            return state;
    }
}
