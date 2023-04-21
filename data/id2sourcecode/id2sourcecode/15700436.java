    private void getProductReportBySLATIdYearMonthReport() throws IOException {
        String xmlFile6Send = System.getenv("SLASOI_HOME") + System.getProperty("file.separator") + "Integration" + System.getProperty("file.separator") + "soap" + System.getProperty("file.separator") + "getProductReportBySLATIdYearMonth.xml";
        URL url6;
        url6 = new URL(bmReportingWSUrl);
        URLConnection connection6 = url6.openConnection();
        HttpURLConnection httpConn6 = (HttpURLConnection) connection6;
        FileInputStream fin6 = new FileInputStream(xmlFile6Send);
        ByteArrayOutputStream bout6 = new ByteArrayOutputStream();
        SOAPClient4XG.copy(fin6, bout6);
        fin6.close();
        byte[] b6 = bout6.toByteArray();
        httpConn6.setRequestProperty("Content-Length", String.valueOf(b6.length));
        httpConn6.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8");
        httpConn6.setRequestProperty("SOAPAction", soapAction);
        httpConn6.setRequestMethod("POST");
        httpConn6.setDoOutput(true);
        httpConn6.setDoInput(true);
        OutputStream out6 = httpConn6.getOutputStream();
        out6.write(b6);
        out6.close();
        InputStreamReader isr6 = new InputStreamReader(httpConn6.getInputStream());
        BufferedReader in6 = new BufferedReader(isr6);
        String inputLine6;
        StringBuffer response6 = new StringBuffer();
        while ((inputLine6 = in6.readLine()) != null) {
            response6.append(inputLine6);
        }
        in6.close();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + "####################################################" + "#################################################\n" + "####################################################" + "#################################################\n" + "Component Name: Business Manager\n" + "Interface Name: getReport\n" + "Operation Name: getProductReportBySLATIdYearMonth\n" + "Input" + "BSLA-ID-BSLATID1\n" + "\n" + "####################################################" + "#################################################\n" + "####################################################" + "#################################################\n" + "######################################## RESPONSE" + "############################################\n\n");
        System.out.println("--------------------------------");
        System.out.println("Response\n" + response6.toString());
    }
