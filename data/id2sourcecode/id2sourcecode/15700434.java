    private void getPenaltiesReportByProductOfferIdPartyIdYearMonthDayReport() throws IOException {
        String xmlFile4Send = System.getenv("SLASOI_HOME") + System.getProperty("file.separator") + "Integration" + System.getProperty("file.separator") + "soap" + System.getProperty("file.separator") + "getPenaltiesReportByProductOfferIdPartyIdYearMonthDay.xml";
        URL url4;
        url4 = new URL(bmReportingWSUrl);
        URLConnection connection4 = url4.openConnection();
        HttpURLConnection httpConn4 = (HttpURLConnection) connection4;
        FileInputStream fin4 = new FileInputStream(xmlFile4Send);
        ByteArrayOutputStream bout4 = new ByteArrayOutputStream();
        SOAPClient4XG.copy(fin4, bout4);
        fin4.close();
        byte[] b4 = bout4.toByteArray();
        httpConn4.setRequestProperty("Content-Length", String.valueOf(b4.length));
        httpConn4.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8");
        httpConn4.setRequestProperty("SOAPAction", soapAction);
        httpConn4.setRequestMethod("POST");
        httpConn4.setDoOutput(true);
        httpConn4.setDoInput(true);
        OutputStream out4 = httpConn4.getOutputStream();
        out4.write(b4);
        out4.close();
        InputStreamReader isr4 = new InputStreamReader(httpConn4.getInputStream());
        BufferedReader in4 = new BufferedReader(isr4);
        String inputLine4;
        StringBuffer response4 = new StringBuffer();
        while ((inputLine4 = in4.readLine()) != null) {
            response4.append(inputLine4);
        }
        in4.close();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + "####################################################" + "#################################################\n" + "####################################################" + "#################################################\n" + "Component Name: Business Manager\n" + "Interface Name: getReport\n" + "Operation Name:" + " getPenaltiesReportByProductOfferIdPartyIdYearMonthDay\n" + "Input" + "ProductOfferID-1\n" + "PartyID-1\n" + "\n" + "####################################################" + "#################################################\n" + "####################################################" + "#################################################\n" + "######################################## RESPONSE" + "############################################\n\n");
        System.out.println("Response\n" + response4.toString());
    }
