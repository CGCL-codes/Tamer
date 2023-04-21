    public FusionTablesPlug() {
        log.trace("init");
        com.googlecode.jamr.PlugUtils pu = new com.googlecode.jamr.PlugUtils();
        com.thoughtworks.xstream.XStream xstream = new com.thoughtworks.xstream.XStream();
        java.io.File file = pu.getConfigFile("fusion");
        try {
            java.io.FileInputStream fis = new java.io.FileInputStream(file);
            ftc = (FusionTablesConfig) xstream.fromXML(fis);
        } catch (java.io.FileNotFoundException fnfe) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            fnfe.printStackTrace(pw);
            log.error(sw.toString());
            ftc = new FusionTablesConfig();
        }
        try {
            postUrl = new java.net.URL("https://www.google.com/fusiontables/api/query?encid=true");
            service = new com.google.gdata.client.GoogleService("fusiontables", "fusiontables.ApiExample");
            service.setUserCredentials(ftc.getEmail(), ftc.getPassword(), com.google.gdata.client.ClientLoginAccountType.GOOGLE);
            java.net.URL url = new java.net.URL("https://www.google.com/fusiontables/api/query?sql=" + java.net.URLEncoder.encode("SHOW TABLES", "UTF-8") + "&encid=true");
            com.google.gdata.client.Service.GDataRequest show = service.getRequestFactory().getRequest(com.google.gdata.client.Service.GDataRequest.RequestType.QUERY, url, com.google.gdata.util.ContentType.TEXT_PLAIN);
            show.execute();
            java.util.List<String[]> rows = getRows(show);
            for (int i = 0; i < rows.size(); i++) {
                String[] data = (String[]) rows.get(i);
                log.warn(data[0]);
                log.warn(data[1]);
                if (data[1].equals("jamr")) {
                    jamrTable = data[0];
                }
            }
            if (jamrTable == null) {
                com.google.gdata.client.Service.GDataRequest request = service.getRequestFactory().getRequest(com.google.gdata.client.Service.GDataRequest.RequestType.INSERT, postUrl, new com.google.gdata.util.ContentType("application/x-www-form-urlencoded"));
                java.io.OutputStreamWriter writer = new java.io.OutputStreamWriter(request.getRequestStream());
                writer.append("sql=" + java.net.URLEncoder.encode("CREATE TABLE jamr (serial:STRING, recorded_at:DATETIME, reading:NUMBER)", "UTF-8"));
                writer.flush();
                request.execute();
                java.util.List<String[]> createRows = getRows(request);
                jamrTable = createRows.get(0)[0];
                log.warn("New table id: " + jamrTable);
            }
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            log.error(sw.toString());
        }
    }
