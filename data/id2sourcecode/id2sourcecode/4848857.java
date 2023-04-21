    private void getDataFromFile(String src, String select) {
        try {
            PrintWriter writer;
            ByteArrayOutputStream xslOut = new ByteArrayOutputStream();
            Transformer transformer;
            String path = testFileDir.getPath() + "/" + src;
            FileInputStream srcFile = new FileInputStream(path);
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(xslOut, "UTF8"));
            writer.println("<xsl:stylesheet version=\"1.0\" " + "xmlns:xsl=\"http://www.w3.org/1999/XSL" + "/Transform\">");
            fOut.println("<xsl:output method=\"xml\"" + " omit-xml-declaration=\"yes\"/>");
            writer.println("<xsl:template match=\"/\">");
            writer.println("<xsl:apply-templates select=\"" + select + "\"/>");
            writer.println("</xsl:template>");
            writer.println("<xsl:template match=\"@*|node()\">");
            writer.println("<xsl:copy>");
            writer.println("<xsl:apply-templates select=\"@*|node()\"/>");
            writer.println("</xsl:copy>");
            writer.println("</xsl:template>");
            writer.println("</xsl:stylesheet>");
            writer.flush();
            transformer = tf.newTransformer(new StreamSource(new ByteArrayInputStream(xslOut.toByteArray())));
            transformer.transform(new StreamSource(srcFile), new StreamResult(result));
            if (readSource) {
                source = new StringBuffer(result.toString("UTF8"));
            } else if (readExpected) {
                expected = new StringBuffer(result.toString("UTF8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
