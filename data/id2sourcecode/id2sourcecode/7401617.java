    public int triggerJob(String jobName) throws IllegalStateException, IOException, JDOMException {
        String url = getTriggeringUrl(jobName);
        HttpGet httpGet = new HttpGet(url);
        System.out.println(url);
        HttpResponse resp = httpClient.execute(httpGet);
        int responseCode = resp.getStatusLine().getStatusCode();
        httpGet.abort();
        if (responseCode == 200) {
            httpGet = new HttpGet(getBuildListUrl(jobName));
            resp = httpClient.execute(httpGet);
            BufferedInputStream bis = new BufferedInputStream(resp.getEntity().getContent());
            SAXBuilder builder = new SAXBuilder(false);
            Document doc = builder.build(bis);
            Element elBuild = (Element) XPath.selectSingleNode(doc, "/*/build['0']");
            if (elBuild != null) {
                Element elNo = elBuild.getChild("number");
                return Integer.parseInt(elNo.getValue());
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }
