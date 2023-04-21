    private void getSLAReport() throws IOException {
        String xmlFile7Send = System.getenv("SLASOI_HOME") + System.getProperty("file.separator") + "Integration" + System.getProperty("file.separator") + "soap" + System.getProperty("file.separator") + "getSLA.xml";
        URL url7;
        url7 = new URL(bmReportingWSUrl);
        URLConnection connection7 = url7.openConnection();
        HttpURLConnection httpConn7 = (HttpURLConnection) connection7;
        FileInputStream fin7 = new FileInputStream(xmlFile7Send);
        ByteArrayOutputStream bout7 = new ByteArrayOutputStream();
        SOAPClient4XG.copy(fin7, bout7);
        fin7.close();
        byte[] b7 = bout7.toByteArray();
        httpConn7.setRequestProperty("Content-Length", String.valueOf(b7.length));
        httpConn7.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8");
        httpConn7.setRequestProperty("SOAPAction", soapAction);
        httpConn7.setRequestMethod("POST");
        httpConn7.setDoOutput(true);
        httpConn7.setDoInput(true);
        OutputStream out7 = httpConn7.getOutputStream();
        out7.write(b7);
        out7.close();
        InputStreamReader isr7 = new InputStreamReader(httpConn7.getInputStream());
        BufferedReader in7 = new BufferedReader(isr7);
        String inputLine7;
        StringBuffer response7 = new StringBuffer();
        while ((inputLine7 = in7.readLine()) != null) {
            response7.append(inputLine7);
        }
        in7.close();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + "####################################################" + "#################################################\n" + "####################################################" + "#################################################\n" + "Component Name: Business Manager\n" + "Interface Name: getReport\n" + "Operation Name: getSLA\n" + "Input" + "ProductOfferID-1\n" + "PartyID-1\n" + "\n" + "####################################################" + "#################################################\n" + "####################################################" + "#################################################\n" + "######################################## RESPONSE" + "############################################\n\n");
        System.out.println("--------------------------------");
        System.out.println("Response\n" + response7.toString());
    }
