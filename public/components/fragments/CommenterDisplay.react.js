import React from 'react';
import { Panel, Image } from 'react-bootstrap';

export default class CommenterDisplay extends React.Component {

  constructor(props) {
    super(props);
  }

  selectThisCommenter() {
    this.props.commenterSelected(this.props.commenter);
  }

  render () {
    const title = (
      <h3>{this.props.commenter.name}</h3>
    );

    return (
      <Panel header={title} onClick={ this.selectThisCommenter.bind(this) }>
        <Image src="this.props.commenter.imageUrl" responsive />
        <p>{this.props.commenter.description}</p>
      </Panel>
    );
  }
}

/*

 id: Long,
 name: String,
 imageUrl: Option[String],
 description: Option[String],
 party: Option[String],
 category: Option[String]
 */