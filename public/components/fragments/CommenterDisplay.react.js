import React from 'react';
import { Panel, Image, Grid, Row, Col } from 'react-bootstrap';

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

    var image;
    if (this.props.commenter.imageUrl) {
      image = (<Image src={this.props.commenter.imageUrl} responsive />);
    } else {
      image = (<Image src="http://tampabayparadeofhomes.com/wp-content/uploads/ngg_featured/placeholder-square.jpg" responsive />);
    }

    return (
      <Panel header={title} onClick={ this.selectThisCommenter.bind(this) }>
          <Row className="show-grid">
            <Col xs={4} md={4}>{image}</Col>
            <Col xs={8} md={8}>{this.props.commenter.description}</Col>
          </Row>
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