    private Reader doCgiFormPost(String urlLoc, Hashtable parameters) throws IOException {
        URL url = new URL(urlLoc);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        PrintWriter out = new PrintWriter(connection.getOutputStream());
        Enumeration e = parameters.keys();
        String paramString = "";
        while (e.hasMoreElements()) {
            String paramName = (String) e.nextElement();
            String paramValue = (String) parameters.get(paramName);
            paramString = paramString + paramName + "=" + paramValue + "&";
        }
        out.print(paramString);
        System.out.println("POST param: " + paramString);
        out.close();
        InputStreamReader in = new InputStreamReader(connection.getInputStream());
        return in;
    }
