import {createStore,applyMiddleware} from 'redux';
import thunk from 'redux-thunk';
import logger from 'redux-logger';
import {userReducer} from './reducer';

export default createStore(userReducer, applyMiddleware(logger, thunk));