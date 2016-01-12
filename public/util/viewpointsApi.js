import Reqwest from 'reqwest';

export default {
  getAllCommenters: () => {
    return Reqwest({
      url: '/api/commenters',
      contentType: 'application/json',
      method: 'get'
    });
  },

  getAllSubjects: () => {
    return Reqwest({
      url: '/api/subjects',
      contentType: 'application/json',
      method: 'get'
    });
  }
}