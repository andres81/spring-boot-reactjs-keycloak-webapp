import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';

import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

import App from './App.js'
import './css/index.css';
import store from './store';
import 'font-awesome/css/font-awesome.min.css'
import {BrowserRouter} from "react-router-dom";

ReactDOM.render(
    <Provider store={store}>
        <BrowserRouter>
            <App />
        </BrowserRouter>
    </Provider>,
    document.getElementById('root')
);
