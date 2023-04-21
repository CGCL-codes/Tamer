    public void sign_in(String username, String password) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost post = new HttpPost(host + LOGIN_API);
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("username", username));
            formparams.add(new BasicNameValuePair("password", password));
            post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            HttpResponse response = httpclient.execute(post);
            this.credential_cookie = response.getFirstHeader("Set-Cookie").getValue();
        } catch (ClientProtocolException e) {
            throw new FreebaseException(e);
        } catch (IOException e) {
            throw new FreebaseException(e);
        } catch (IllegalStateException e) {
            throw new FreebaseException(e);
        } catch (ClassCastException e) {
            throw new FreebaseException(e);
        }
    }
