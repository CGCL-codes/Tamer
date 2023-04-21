    protected List<PDBResult> getDescriptions(List<String> ids) throws Exception {
        List<PDBResult> results = new LinkedList<PDBResult>();
        String idsAsString = listToCSV(ids.subList(0, Math.min(ids.size(), 50)));
        Log.i("JMOL", "Getting descriptions for " + idsAsString);
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(String.format("http://www.rcsb.org/pdb/rest/customReport?pdbids=%s&customReportColumns=structureId,structureTitle", idsAsString));
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(EntityUtils.toString(entity)));
            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName("record");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element e = (Element) nodes.item(i);
                String id = e.getChildNodes().item(1).getFirstChild().getNodeValue();
                String desc = e.getChildNodes().item(3).getFirstChild().getNodeValue();
                results.add(new PDBResult(id, desc));
            }
        }
        return results;
    }
