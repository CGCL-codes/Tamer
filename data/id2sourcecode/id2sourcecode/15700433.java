    private void getPenaltiesReportByProductOfferIdPartyIdYearMonthReport() throws IOException {
        String xmlFile3Send = System.getenv("SLASOI_HOME") + System.getProperty("file.separator") + "Integration" + System.getProperty("file.separator") + "soap" + System.getProperty("file.separator") + "getPenaltiesReportByProductOfferIdPartyIdYearMonth.xml";
        URL url3;
        url3 = new URL(bmReportingWSUrl);
        URLConnection connection3 = url3.openConnection();
        HttpURLConnection httpConn3 = (HttpURLConnection) connection3;
        FileInputStream fin3 = new FileInputStream(xmlFile3Send);
        ByteArrayOutputStream bout3 = new ByteArrayOutputStream();
        SOAPClient4XG.copy(fin3, bout3);
        fin3.close();
        byte[] b3 = bout3.toByteArray();
        httpConn3.setRequestProperty("Content-Length", String.valueOf(b3.length));
        httpConn3.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8");
        httpConn3.setRequestProperty("SOAPAction", soapAction);
        httpConn3.setRequestMethod("POST");
        httpConn3.setDoOutput(true);
        httpConn3.setDoInput(true);
        OutputStream out3 = httpConn3.getOutputStream();
        out3.write(b3);
        out3.close();
        InputStreamReader isr3 = new InputStreamReader(httpConn3.getInputStream());
        BufferedReader in3 = new BufferedReader(isr3);
        String inputLine3;
        StringBuffer response3 = new StringBuffer();
        while ((inputLine3 = in3.readLine()) != null) {
            response3.append(inputLine3);
        }
        in3.close();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + "####################################################" + "#################################################\n" + "####################################################" + "#################################################\n" + "Component Name: Business Manager\n" + "Interface Name: getReport\n" + "Operation Name:" + " getPenaltiesReportByProduct" + "OfferIdPartyIdYearMonth\n" + "Input" + "ProductOfferID-1\n" + "PartyID-1\n" + "\n" + "####################################################" + "#################################################\n" + "####################################################" + "#################################################\n" + "######################################## RESPONSE" + "############################################\n\n");
        System.out.println("--------------------------------");
        System.out.println("Response\n" + response3.toString());
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }
