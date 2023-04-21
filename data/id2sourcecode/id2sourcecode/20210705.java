    private void getSession() {
        if (m_client == null) {
            final HttpPost post = new HttpPost(m_baseURL + "/REST/1.0/user/" + m_user);
            final List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user", m_user));
            params.add(new BasicNameValuePair("pass", m_password));
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
                post.setEntity(entity);
            } catch (final UnsupportedEncodingException e) {
                LogUtils.warnf(this, e, "unsupported encoding exception for UTF-8 -- WTF?!");
            }
            try {
                final HttpResponse response = getClient().execute(post);
                int responseCode = response.getStatusLine().getStatusCode();
                if (responseCode != HttpStatus.SC_OK) {
                    throw new RequestTrackerException("Received a non-200 response code from the server: " + responseCode);
                } else {
                    if (response.getEntity() != null) {
                        response.getEntity().consumeContent();
                    }
                    LogUtils.warnf(this, "got user session for username: %s", m_user);
                }
            } catch (final Exception e) {
                LogUtils.warnf(this, e, "Unable to get session (by requesting user details)");
            }
        }
    }
