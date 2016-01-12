import React from 'react';
import { Input, ButtonInput } from 'react-bootstrap';

export default class CommenterEdit extends React.Component {

  constructor(props) {
    super(props);
    if (props.commenter) {
      this.state = {modifiedCommenter: Object.assign({}, props.commenter)};
    } else {
      this.state = {modifiedCommenter: {}};
    }
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.commenter !== nextProps.commenter) {
      if (nextProps.commenter) {
        this.state = {modifiedCommenter: Object.assign({}, nextProps.commenter)};
      } else {
        this.state = {modifiedCommenter: {}};
      }
    }
  }

  updateName(e) {
    const updated = this.state.modifiedCommenter;
    updated.name = e.target.value;
    this.setState({modifiedCommenter: updated});
  }

  updateDescription(e) {
    const updated = this.state.modifiedCommenter;
    updated.description = e.target.value;
    this.setState({modifiedCommenter: updated});
  }

  updateImage(e) {
    const updated = this.state.modifiedCommenter;
    updated.imageUrl = e.target.value;
    this.setState({modifiedCommenter: updated});
  }

  updateParty(e) {
    const updated = this.state.modifiedCommenter;
    updated.party = e.target.value;
    this.setState({modifiedCommenter: updated});
  }

  saveModifications() {
    this.props.saveCommenter(this.state.modifiedCommenter);
  }

  render () {

    if (!this.props.commenter) {
      return false;
    }

    return (
      <div>
        <form className="form-horizontal">
          <Input type="text" label="Name" value={this.state.modifiedCommenter.name} labelClassName="col-xs-2" wrapperClassName="col-xs-10" onChange={this.updateName.bind(this)} />
          <Input type="text" label="Description" value={this.state.modifiedCommenter.description} labelClassName="col-xs-2" wrapperClassName="col-xs-10" onChange={this.updateDescription.bind(this)} />
          <Input type="text" label="Image Url" value={this.state.modifiedCommenter.imageUrl} labelClassName="col-xs-2" wrapperClassName="col-xs-10" onChange={this.updateImage.bind(this)}/>
          <Input type="text" label="Party" value={this.state.modifiedCommenter.party} labelClassName="col-xs-2" wrapperClassName="col-xs-10" onChange={this.updateParty.bind(this)}/>
          <ButtonInput bsStyle="primary" onClick={this.saveModifications.bind(this)} >Save commenter</ButtonInput>
          <ButtonInput bsStyle="primary" onClick={this.props.cancelCommenterEdit} >Cancel</ButtonInput>
        </form>
      </div>
    );
  }
}
