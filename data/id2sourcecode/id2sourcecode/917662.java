    private URLConnection getServletConnection(String servletname) throws MalformedURLException, IOException {
        URL urlServlet = new URL(getCodeBase(), servletname);
        URLConnection con = urlServlet.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setRequestProperty("Content-Type", "application/x-java-serialized-object");
        return con;
    }
