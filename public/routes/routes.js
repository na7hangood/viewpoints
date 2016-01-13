import React        from 'react';
import { Route, IndexRoute } from 'react-router';

import ReactApp     from '../components/reactApp.react';
import Commenters   from '../components/Commenters.react';
import Subjects     from '../components/Subjects.react';
import Categories   from '../components/Categories.react';
import Home         from '../components/Home.react';

export default [
    <Route path="/" component={ReactApp}>
          <Route name="commenters" path="/commenters" component={Commenters} />
          <Route name="subjects" path="/subjects" component={Subjects} />
          <Route name="categories" path="/categories" component={Categories} />
          <IndexRoute component={Home}/>
    </Route>
];
