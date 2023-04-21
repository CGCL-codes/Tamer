    public static Correction[] suggestions(String text, String lang, String hl) throws CorrectionException {
        try {
            StringBuffer request = new StringBuffer();
            request.append("<spellrequest textalreadyclipped=\"0\" ignoredups=\"1\" ignoredigits=\"1\" ignoreallcaps=\"0\"><text>");
            request.append(ERXStringUtilities.escapeNonXMLChars(text));
            request.append("</text></spellrequest>");
            URL url = new URL("https://www.google.com/tbproxy/spell?lang=" + lang + "&hl=" + lang);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(request.toString());
            out.close();
            Correction[] corrections;
            InputStream in = connection.getInputStream();
            try {
                Document responseDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
                responseDocument.normalize();
                NodeList correctionNodes = responseDocument.getElementsByTagName("c");
                int correctionCount = correctionNodes.getLength();
                corrections = new Correction[correctionCount];
                for (int correctionNum = 0; correctionNum < correctionCount; correctionNum++) {
                    Node correctionNode = correctionNodes.item(correctionNum);
                    if (correctionNode instanceof Element) {
                        Element correctionElement = (Element) correctionNode;
                        String correctionsStr = correctionElement.getChildNodes().item(0).getNodeValue();
                        int offset = Integer.parseInt(correctionElement.getAttribute("o"));
                        int length = Integer.parseInt(correctionElement.getAttribute("l"));
                        int confidence = Integer.parseInt(correctionElement.getAttribute("s"));
                        String[] correctionStrs = correctionsStr.split("\t");
                        corrections[correctionNum] = new Correction(offset, length, confidence, correctionStrs);
                    }
                }
            } finally {
                in.close();
            }
            return corrections;
        } catch (Exception e) {
            throw new CorrectionException("Failed to correct spelling of '" + text + "'.", e);
        }
    }
