    protected Document post(String location, String content) throws ApplicationException {
        Document doc = null;
        try {
            URL url = new URL(location);
            String encoding = new BASE64Encoder().encode(configuration.getBasecampPassword().getBytes());
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setRequestProperty("Authorization", "Basic " + encoding);
            uc.setRequestProperty("Accept", "application/xml");
            uc.setRequestProperty("Content-Type", "application/xml");
            uc.setRequestProperty("X-POST_DATA_FORMAT", "xml");
            uc.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(uc.getOutputStream());
            out.write("<request><content>" + content + "</content></request>");
            out.close();
            doc = XmlUtils.readDocumentFromInputStream(uc.getInputStream());
            System.out.println("result: " + XmlUtils.toString(doc));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApplicationException(e);
        }
        return doc;
    }
