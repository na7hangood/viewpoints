import React from 'react';
import { render } from 'react-dom';
import Router from 'react-router';
import routes from './routes/routes';

render(<Router routes={routes} />, document.getElementById('react-mount'));

