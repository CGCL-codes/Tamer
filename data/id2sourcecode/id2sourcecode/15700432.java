    private void getGTStatusBySLAIdGTKPIReport() throws IOException {
        String xmlFile2Send = System.getenv("SLASOI_HOME") + System.getProperty("file.separator") + "Integration" + System.getProperty("file.separator") + "soap" + System.getProperty("file.separator") + "getGTStatusBySLAIdGTKPI.xml";
        URL url2;
        url2 = new URL(bmReportingWSUrl);
        URLConnection connection2 = url2.openConnection();
        HttpURLConnection httpConn2 = (HttpURLConnection) connection2;
        FileInputStream fin2 = new FileInputStream(xmlFile2Send);
        ByteArrayOutputStream bout2 = new ByteArrayOutputStream();
        SOAPClient4XG.copy(fin2, bout2);
        fin2.close();
        byte[] b2 = bout2.toByteArray();
        httpConn2.setRequestProperty("Content-Length", String.valueOf(b2.length));
        httpConn2.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8");
        httpConn2.setRequestProperty("SOAPAction", soapAction);
        httpConn2.setRequestMethod("POST");
        httpConn2.setDoOutput(true);
        httpConn2.setDoInput(true);
        OutputStream out2 = httpConn2.getOutputStream();
        out2.write(b2);
        out2.close();
        InputStreamReader isr2 = new InputStreamReader(httpConn2.getInputStream());
        BufferedReader in2 = new BufferedReader(isr2);
        String inputLine2;
        StringBuffer response2 = new StringBuffer();
        while ((inputLine2 = in2.readLine()) != null) {
            response2.append(inputLine2);
        }
        in2.close();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + "####################################################" + "#################################################\n" + "####################################################" + "#################################################\n" + "Component Name: Business Manager\n" + "Interface Name: getReport\n" + "Operation Name: getGTStatusBySLAIdGTKPI\n" + "Input" + "ProductOfferID-1\n" + "PartyID-1\n" + "\n" + "####################################################" + "#################################################\n" + "####################################################" + "#################################################\n" + "######################################## RESPONSE" + "############################################\n\n");
        System.out.println("--------------------------------");
        System.out.println("Response\n" + response2.toString());
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }
