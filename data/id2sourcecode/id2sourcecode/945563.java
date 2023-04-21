    private void makeHtml(Node _baseNode) throws TransformerException, RESyntaxException, IOException {
        String urlName = "";
        baseNode = _baseNode;
        StringBuffer filenameBuf = new StringBuffer();
        NodeList nl1 = XPathAPI.selectNodeList(baseNode, filenameXpath);
        for (int i = 0; i < nl1.getLength(); i++) {
            Node nd1 = nl1.item(i);
            if (filenameBuf.length() > 0) filenameBuf.append("-");
            filenameBuf.append(getNodeTextValue(nd1));
        }
        String outputFileName = outDir + File.separator + filenameBuf.toString() + ".htm";
        NodeList nl2 = XPathAPI.selectNodeList(baseNode, urlNameXpath);
        for (int j = 0; j < nl2.getLength(); j++) {
            Node nd2 = nl2.item(j);
            urlName = getNodeTextValue(nd2);
        }
        URL url = new URL(urlName);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer htmlString = new StringBuffer();
        String line = "";
        while ((line = br.readLine()) != null) {
            htmlString.append(line + "\n");
        }
        br.close();
        PrintWriter pw = prepareHtml(outputFileName);
        pw.println(htmlString.toString());
        pw.close();
    }
