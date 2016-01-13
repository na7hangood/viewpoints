import React from 'react';
import { ListGroup } from 'react-bootstrap';
import ViewpointDisplay from './ViewpointDisplay.react';

export default class ViewpointList extends React.Component {

  constructor(props) {
    super(props);
  }

  render () {

    if (!this.props.viewpoints || !this.props.viewpoints.length) {
      return (
        <p>There are no viewpoints yet</p>
      )
    }

    const self = this;
    const viewpointNodes = this.props.viewpoints.map(function(viewpoint) {
      return (
        <ViewpointDisplay viewpoint={viewpoint} viewpointSelected={self.props.viewpointSelected} key={viewpoint.id} />
      );
    });

    return (
      <div>
        {viewpointNodes}
      </div>
    );
  }
}