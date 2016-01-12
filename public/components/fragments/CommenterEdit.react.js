import React from 'react';

export default class CommenterEdit extends React.Component {

  constructor(props) {
    super(props);
  }

  render () {

    if (!this.props.commenter) {
      return false;
    }

    return (
      <div>
        This is an edit form.
      </div>
    );
  }
}