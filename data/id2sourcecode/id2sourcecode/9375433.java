    public String generate(Object info) {
        Tester test = (Tester) info;
        HttpServletRequest request = test.getRequest();
        StringBuffer xml = null;
        try {
            Iterator sessionFromAllNodes = test.testReplication(PageRequestProcessor.getRequestURI(request, true)).iterator();
            xml = new StringBuffer("<replicationTest>");
            while (sessionFromAllNodes.hasNext()) {
                try {
                    SessionInfo session = (SessionInfo) sessionFromAllNodes.next();
                    if (session.getErrorMessage() != null) {
                        xml.append("<error>");
                        xml.append(session.getErrorMessage());
                        xml.append("</error>");
                    }
                    JSONObject jo = new JSONObject(session);
                    xml.append(XML.toString(jo, "session"));
                } catch (Exception e) {
                    LOGGER.warn("[sessionmon]Could not generate JSONObject from SessionInfo object");
                }
            }
            xml.append("</replicationTest>");
        } catch (OnlyOneNodeException e) {
            xml = new StringBuffer("<replicationTest><error>Only one node was found. Please list all nodes in your web.xml configuration if you're running clustered environment.</error></replicationTest>");
            LOGGER.info("[sessionmon]Replication test did not run: Only one node was found. Please list all nodes in your web.xml configuration if you're running clustered environment.");
        } catch (Exception e) {
            xml = new StringBuffer("<replicationTest><error>Exception Occurred:<br/>" + e.getMessage() + "</error></replicationTest>");
            LOGGER.error("[sessionmon]Replication test did not run: " + e.getMessage(), e);
        }
        try {
            StringReader reader = new StringReader(xml.toString());
            StringWriter writer = new StringWriter();
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(File.readFileAsInputStream("report/testReplicationReportHTML.xsl")));
            transformer.transform(new javax.xml.transform.stream.StreamSource(reader), new javax.xml.transform.stream.StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            LOGGER.error("[sessionmon]Could not generate HTML: " + e.getMessage(), e);
        }
        return null;
    }
