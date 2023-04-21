    public List<Job> listJobs() throws ClientProtocolException, IOException, JDOMException {
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(this.hostname, this.port), new UsernamePasswordCredentials(this.user, this.password));
        HttpGet httpGet = new HttpGet(PROTOCOL_PREFIX + this.hostname + ":" + port + servicePath + "/api/xml");
        List<Job> lstRet = new LinkedList<Job>();
        HttpResponse resp = httpClient.execute(httpGet);
        int responseCode = resp.getStatusLine().getStatusCode();
        if (responseCode == 200) {
            String message = resp.getStatusLine().getReasonPhrase();
            HttpEntity entity = resp.getEntity();
            InputStream is = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            Document doc = builder.build(br);
            List<Element> lstJobs = XPath.selectNodes(doc, "/hudson/job");
            for (int i = 0; i < lstJobs.size(); i++) {
                Element elJob = lstJobs.get(i);
                Job aJob = new Job(elJob.getChild("name").getValue(), elJob.getChild("url").getValue(), elJob.getChild("color").getValue());
                lstRet.add(aJob);
            }
        } else {
        }
        return lstRet;
    }
