import React from 'react';
import CommenterDisplay from './CommenterDisplay.react';

export default class CommenterList extends React.Component {

  constructor(props) {
    super(props);
  }

  render () {

    const self = this;
    const commenterNodes = this.props.commenters.map(function(commenter) {
      return (
        <CommenterDisplay commenter={commenter} commenterSelected={self.props.commenterSelected} key={commenter.id} />
      );
    });

    if (!this.props.commenters || !this.props.commenters.length) {
      return (
        <p>There are no commenters defined</p>
      )
    }

    return (
      <div>
        {commenterNodes}
      </div>
    );
  }
}