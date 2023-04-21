    private void getGTStatusBySLAIdGTReport() throws IOException {
        String xmlFile1Send = System.getenv("SLASOI_HOME") + System.getProperty("file.separator") + "Integration" + System.getProperty("file.separator") + "soap" + System.getProperty("file.separator") + "getGTStatusBySLAIdGT.xml";
        URL url;
        url = new URL(bmReportingWSUrl);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) connection;
        FileInputStream fin = new FileInputStream(xmlFile1Send);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        SOAPClient4XG.copy(fin, bout);
        fin.close();
        byte[] b = bout.toByteArray();
        httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
        httpConn.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8");
        httpConn.setRequestProperty("SOAPAction", soapAction);
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        OutputStream out = httpConn.getOutputStream();
        out.write(b);
        out.close();
        InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
        BufferedReader in = new BufferedReader(isr);
        String inputLine;
        StringBuffer response1 = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response1.append(inputLine);
        }
        in.close();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + "####################################################" + "#################################################\n" + "####################################################" + "#################################################\n" + "Component Name: Business Manager\n" + "Interface Name: getReport\n" + "Operation Name: GetGTStatusBySLAIdGT\n" + "Input" + "BSLA Id-BSLATID1\n" + "GuranteeTerm-1\n" + "\n" + "####################################################" + "#################################################\n" + "####################################################" + "#################################################\n" + "######################################## RESPONSE" + "############################################\n\n");
        System.out.println("--------------------------------");
        System.out.println("Response\n" + response1.toString());
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }
