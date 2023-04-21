    private void getProductReportByProductOfferIdYearMonthReport() throws IOException {
        String xmlFile5Send = System.getenv("SLASOI_HOME") + System.getProperty("file.separator") + "Integration" + System.getProperty("file.separator") + "soap" + System.getProperty("file.separator") + "getProductReportByProductOfferIdYearMonth.xml";
        URL url5;
        url5 = new URL(bmReportingWSUrl);
        URLConnection connection5 = url5.openConnection();
        HttpURLConnection httpConn5 = (HttpURLConnection) connection5;
        FileInputStream fin5 = new FileInputStream(xmlFile5Send);
        ByteArrayOutputStream bout5 = new ByteArrayOutputStream();
        SOAPClient4XG.copy(fin5, bout5);
        fin5.close();
        byte[] b5 = bout5.toByteArray();
        httpConn5.setRequestProperty("Content-Length", String.valueOf(b5.length));
        httpConn5.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8");
        httpConn5.setRequestProperty("SOAPAction", soapAction);
        httpConn5.setRequestMethod("POST");
        httpConn5.setDoOutput(true);
        httpConn5.setDoInput(true);
        OutputStream out5 = httpConn5.getOutputStream();
        out5.write(b5);
        out5.close();
        InputStreamReader isr5 = new InputStreamReader(httpConn5.getInputStream());
        BufferedReader in5 = new BufferedReader(isr5);
        String inputLine5;
        StringBuffer response5 = new StringBuffer();
        while ((inputLine5 = in5.readLine()) != null) {
            response5.append(inputLine5);
        }
        in5.close();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + "####################################################" + "#################################################\n" + "####################################################" + "#################################################\n" + "Component Name: Business Manager\n" + "Interface Name: getReport\n" + "Operation Name: getProductReportByProductOfferIdYearMonth\n" + "Input" + "ProductOfferID-1\n" + "PartyID-1\n" + "\n" + "####################################################" + "#################################################\n" + "####################################################" + "#################################################\n" + "######################################## RESPONSE" + "############################################\n\n");
        System.out.println("Response\n" + response5.toString());
    }
