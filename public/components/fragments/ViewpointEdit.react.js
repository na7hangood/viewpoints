import React from 'react';
import { Input, ButtonInput } from 'react-bootstrap';

export default class ViewpointEdit extends React.Component {

  constructor(props) {
    super(props);
    if (props.viewpoint) {
      this.state = {modifiedViewpoint: Object.assign({}, props.commenter)};
    } else {
      this.state = {modifiedViewpoint: {}};
    }
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.viewpoint !== nextProps.viewpoint) {
      if (nextProps.viewpoint) {
        this.state = {modifiedViewpoint: Object.assign({}, nextProps.viewpoint)};
      } else {
        this.state = {modifiedViewpoint: {}};
      }
    }
  }

  updateLink(e) {
    const updated = this.state.modifiedViewpoint;
    updated.link = e.target.value;
    this.setState({modifiedViewpoint: updated});
  }
  //
  //updateDescription(e) {
  //  const updated = this.state.modifiedCommenter;
  //  updated.description = e.target.value;
  //  this.setState({modifiedCommenter: updated});
  //}
  //
  //updateImage(e) {
  //  const updated = this.state.modifiedCommenter;
  //  updated.imageUrl = e.target.value;
  //  this.setState({modifiedCommenter: updated});
  //}
  //
  //updateParty(e) {
  //  const updated = this.state.modifiedCommenter;
  //  updated.party = e.target.value;
  //  this.setState({modifiedCommenter: updated});
  //}

  saveModifications() {
    this.props.saveViewpoint(this.state.modifiedViewpoint);
  }

  render () {

    if (!this.props.viewpoint) {
      return false;
    }

    return (
      <div>
        <form className="form-horizontal">
          <Input type="text" label="Link" value={this.state.modifiedViewpoint.link} labelClassName="col-xs-2" wrapperClassName="col-xs-10" onChange={this.updateLink.bind(this)}/>
          <ButtonInput bsStyle="primary" onClick={this.saveModifications.bind(this)} >Save viewpoint</ButtonInput>
          <ButtonInput bsStyle="primary" onClick={this.props.cancelViewpointEdit} >Cancel</ButtonInput>
        </form>
      </div>
    );
  }

}