    public static List<User> fetchFriendUpdates(Account account, String authtoken, Date lastUpdated) throws JSONException, ParseException, IOException, AuthenticationException {
        final ArrayList<User> friendList = new ArrayList<User>();
        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAM_USERNAME, account.name));
        params.add(new BasicNameValuePair(PARAM_PASSWORD, authtoken));
        if (lastUpdated != null) {
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            params.add(new BasicNameValuePair(PARAM_UPDATED, formatter.format(lastUpdated)));
        }
        Log.i(TAG, params.toString());
        HttpEntity entity = null;
        entity = new UrlEncodedFormEntity(params);
        final HttpPost post = new HttpPost(FETCH_FRIEND_UPDATES_URI);
        post.addHeader(entity.getContentType());
        post.setEntity(entity);
        maybeCreateHttpClient();
        final HttpResponse resp = mHttpClient.execute(post);
        final String response = EntityUtils.toString(resp.getEntity());
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new ByteArrayInputStream(response.getBytes()));
                NodeList nodeList = document.getElementsByTagName("rider");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    User user = User.valueOf(nodeList.item(i));
                    if (user != null) {
                        friendList.add(user);
                    }
                }
            } catch (SAXException saxE) {
                Log.e(TAG, response, saxE);
            } catch (ParserConfigurationException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        } else {
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                Log.e(TAG, "Authentication exception in fetching remote contacts");
                throw new AuthenticationException();
            } else {
                Log.e(TAG, "Server error in fetching remote contacts: " + resp.getStatusLine());
                throw new IOException();
            }
        }
        return friendList;
    }
