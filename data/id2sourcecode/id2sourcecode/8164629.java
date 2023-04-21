    public String get_blob(String id, JSON options) {
        if (id == null || id.trim().length() == 0) throw new FreebaseException("You must provide the id of the blob you want");
        String path = BLOB_API_PREFIX;
        String mode = (options != null && options.has("mode")) ? options.get("mode").string() : "raw";
        if ("raw".equals(mode) || "unsafe".equals(mode) || "blurb".equals(mode)) {
            path += mode;
        } else {
            throw new FreebaseException("Invalid mode; must be 'raw' or 'blurb' or 'unsafe'");
        }
        path += id;
        List<NameValuePair> qparams = transform_params(options);
        String url = host + path + "?" + URLEncodedUtils.format(qparams, "UTF-8");
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpRequestBase method = new HttpGet(url);
            method.setHeader("X-Requested-With", "1");
            HttpResponse response = httpclient.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return stringify(new InputStreamReader(entity.getContent(), "UTF-8"));
            } else {
                throw new FreebaseException("Response was empty");
            }
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
