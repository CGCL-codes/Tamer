    private JSON urlfetch(URI uri, int protocol, Map<String, String> headers, CharSequence content, boolean sign) {
        JSON result = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpRequestBase method = null;
            if (protocol == GET) {
                method = new HttpGet(uri);
            } else {
                HttpPost httppost = new HttpPost(uri);
                httppost.setEntity(new StringEntity(content.toString()));
                method = httppost;
            }
            for (Map.Entry<String, String> e : headers.entrySet()) {
                method.setHeader(e.getKey(), e.getValue());
            }
            if (sign) {
                sign(method);
            }
            HttpResponse response = httpclient.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = JSON.parse(new InputStreamReader(entity.getContent(), "UTF-8"));
            } else {
                result = JSON.o();
            }
        } catch (ClientProtocolException e) {
            throw new FreebaseException(e);
        } catch (IOException e) {
            throw new FreebaseException(e);
        } catch (IllegalStateException e) {
            throw new FreebaseException(e);
        } catch (ParseException e) {
            throw new FreebaseException(e);
        } catch (ClassCastException e) {
            throw new FreebaseException(e);
        }
        return check_result(result);
    }
