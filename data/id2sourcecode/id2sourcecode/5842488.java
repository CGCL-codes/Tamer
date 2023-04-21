    protected void postUpdateXml(RequestWriter requestWriter) throws SolrClientException, SolrServerException {
        try {
            HttpURLConnection urlc = (HttpURLConnection) solrUpdateUrl.openConnection();
            urlc.setRequestMethod("POST");
            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            urlc.setUseCaches(false);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            urlc.setRequestProperty("User-Agent", AGENT);
            OutputStream out = urlc.getOutputStream();
            try {
                Writer writer = new OutputStreamWriter(out, "utf-8");
                try {
                    requestWriter.writeRequest(writer);
                    out.flush();
                } catch (Exception e) {
                    throw new SolrClientException("RequestWriter failed to write request.", e);
                } finally {
                    writer.close();
                }
            } finally {
                out.close();
            }
            InputStream inputStream = urlc.getInputStream();
            try {
                Reader reader = new InputStreamReader(inputStream);
                try {
                    XmlPullParser xpp;
                    try {
                        xpp = factory.newPullParser();
                        xpp.setInput(reader);
                        xpp.nextTag();
                    } catch (XmlPullParserException e) {
                        throw new SolrClientException("XML parsing exception in solr client", e);
                    }
                    LOG.debug("xml element name: " + xpp.getName());
                    System.err.println("xml element name: " + xpp.getName());
                    if (!"response".equals(xpp.getName())) {
                        throw new SolrClientException("Result from server is not rooted with a <result> tag.");
                    }
                    try {
                        xpp.nextTag();
                        xpp.nextTag();
                    } catch (XmlPullParserException ex) {
                    }
                    String statusString = "0";
                    int status = Integer.parseInt(statusString);
                    if (status != 0) {
                        try {
                            StringWriter str = new StringWriter();
                            try {
                                requestWriter.writeRequest(str);
                            } catch (Exception ex) {
                            }
                            throw new SolrServerException("Server returned non-zero status: ", status, xpp.nextText(), str.toString());
                        } catch (XmlPullParserException e) {
                            throw new SolrClientException("XML parsing exception in solr client", e);
                        }
                    }
                } finally {
                    reader.close();
                }
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new SolrClientException("I/O exception in solr client", e);
        }
    }
