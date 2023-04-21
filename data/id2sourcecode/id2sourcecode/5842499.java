    public QueryResults query(SolrQuery query) throws SolrClientException, SolrServerException {
        try {
            URL qurl = new URL(solrSearchUrl.toExternalForm() + "?" + query.getQueryString());
            HttpURLConnection urlc = (HttpURLConnection) qurl.openConnection();
            urlc.setRequestMethod("GET");
            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            urlc.setUseCaches(false);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestProperty("User-Agent", AGENT);
            if (urlc.getResponseCode() != 200) {
                InputStream input = urlc.getErrorStream();
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                long count = 0;
                int n = 0;
                while (-1 != (n = input.read(buffer))) {
                    output.write(buffer, 0, n);
                    count += n;
                }
                throw new SolrServerException("non XML result", urlc.getResponseCode(), output.toString(), qurl.toExternalForm());
            }
            InputStream inputStream = urlc.getInputStream();
            try {
                Reader reader = new InputStreamReader(inputStream);
                QueryResults res = parser.process(reader);
                res.setSolrURL(qurl);
                res.setQuery(query);
                return res;
            } catch (XmlPullParserException e) {
                throw new SolrClientException("error parsing XML", e);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new SolrClientException("I/O exception in solr client", e);
        }
    }
