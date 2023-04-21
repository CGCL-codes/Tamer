    private static Document getXML(String methodParms, String whichXML) throws Exception {
        System.out.println("Receaved request to fetch " + whichXML);
        DocumentBuilder builder = null;
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        URL url = new URL(whichXML);
        URLConnection connection = url.openConnection();
        ((HttpURLConnection) connection).setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        System.out.println("Got connection to " + whichXML);
        OutputStreamWriter outStream = new OutputStreamWriter(connection.getOutputStream());
        PrintWriter output = new PrintWriter(outStream);
        output.write(methodParms);
        output.flush();
        output.close();
        outStream.close();
        System.out.println("Wrote request to " + whichXML);
        StringBuilder sb = new StringBuilder();
        String buf = "";
        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        System.out.println("Reading XML from site");
        while ((buf = input.readLine()) != null) sb.append(buf).append("\n");
        buf = sb.toString();
        if (buf.contains("<error code=\"")) throw new Exception("EVE XML returned an error.\n" + buf);
        return builder.parse(new ByteArrayInputStream(buf.getBytes("UTF-8")));
    }
