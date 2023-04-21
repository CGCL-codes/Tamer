    public static List<User.Status> fetchFriendStatuses(Account account, String authtoken) throws JSONException, ParseException, IOException, AuthenticationException {
        final ArrayList<User.Status> statusList = new ArrayList<User.Status>();
        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAM_USERNAME, account.name));
        params.add(new BasicNameValuePair(PARAM_PASSWORD, authtoken));
        HttpEntity entity = null;
        entity = new UrlEncodedFormEntity(params);
        final HttpPost post = new HttpPost(FETCH_STATUS_URI);
        post.addHeader(entity.getContentType());
        post.setEntity(entity);
        maybeCreateHttpClient();
        final HttpResponse resp = mHttpClient.execute(post);
        final String response = EntityUtils.toString(resp.getEntity());
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            for (int i = 0; i < statusList.size(); i++) {
                statusList.add(new User.Status(i, "OK"));
            }
        } else {
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                Log.e(TAG, "Authentication exception in fetching friend status list");
                throw new AuthenticationException();
            } else {
                Log.e(TAG, "Server error in fetching friend status list");
                throw new IOException();
            }
        }
        return statusList;
    }
