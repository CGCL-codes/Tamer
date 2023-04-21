    public Build getBuild(String jobName, int buildNumber) throws ClientProtocolException, IOException, JDOMException {
        String url = getBuildUrl(jobName, buildNumber);
        HttpGet httpGet = new HttpGet(url);
        HttpResponse resp = httpClient.execute(httpGet);
        int respCode = resp.getStatusLine().getStatusCode();
        if (respCode == 200) {
            BufferedInputStream bis = new BufferedInputStream(resp.getEntity().getContent());
            SAXBuilder builder = new SAXBuilder(false);
            Document doc = builder.build(bis);
            Element root = doc.getRootElement();
            Build build = new Build();
            build.setBuilding(Boolean.parseBoolean(root.getChildText("building")));
            build.setDuration(Long.parseLong(root.getChildText("duration")));
            build.setFullDisplayName(root.getChildText("fullDisplayName"));
            build.setId(root.getChildText("id"));
            build.setNumber(Integer.parseInt(root.getChildText("number")));
            build.setResult(root.getChildText("result"));
            build.setTimestemp(root.getChildText("timpestamp"));
            build.setUrl(root.getChildText("url"));
            return build;
        } else {
            return null;
        }
    }
