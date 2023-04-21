    private ResponseXML postResponseXml(String path, List<NameValuePair> qparams) {
        HttpClient httpclient = null;
        InputStream in = null;
        ResponseXML res = null;
        try {
            httpclient = new DefaultHttpClient();
            URI uri = URIUtils.createURI("http", HTTP_HOST_NAME, -1, path, URLEncodedUtils.format(qparams, "UTF-8"), null);
            HttpPost httpPost = new HttpPost(uri);
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            in = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder buf = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                buf.append(str);
                buf.append(System.getProperty("line.separator"));
            }
            reader.close();
            res = new ResponseXML(buf.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return res;
    }
