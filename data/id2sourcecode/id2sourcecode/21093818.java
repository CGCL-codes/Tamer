    public String generate(Object info) {
        String html = null;
        try {
            SessionInfo sessionInfo = (SessionInfo) info;
            Report report = ReportFactory.create(CommandEnum.DUMP, ReportFactory.REPORT_TYPE_XML);
            StringReader reader = new StringReader(report.generate(sessionInfo));
            StringWriter writer = new StringWriter();
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(File.readFileAsInputStream("report/sessionDumpReportHTML.xsl")));
            transformer.transform(new javax.xml.transform.stream.StreamSource(reader), new javax.xml.transform.stream.StreamResult(writer));
            html = writer.toString();
        } catch (Exception e) {
            html = "Sorry, internal error has occured.";
        }
        return html;
    }
