    protected FileSearch sendRequestToServletAndRetreiveResponse(String urlString, FileSearch fileSearch) throws IOException {
        System.out.print("creating URL...");
        URL urlOfServlet = null;
        try {
            urlOfServlet = new URL(urlString);
        } catch (java.net.MalformedURLException e) {
            throw new IOException(urlString + " is not a valid URL");
        }
        System.out.println("done");
        System.out.flush();
        System.out.print("connecting to servlet...");
        java.net.URLConnection connectionToServlet = urlOfServlet.openConnection();
        System.out.println("done");
        System.out.flush();
        System.out.print("setting up reading & writing...");
        connectionToServlet.setDoInput(true);
        connectionToServlet.setDoOutput(true);
        System.out.println("done");
        System.out.flush();
        System.out.print("turning off cache...");
        connectionToServlet.setUseCaches(false);
        connectionToServlet.setDefaultUseCaches(false);
        System.out.println("done");
        System.out.flush();
        System.out.println("setting up for binary data");
        connectionToServlet.setRequestProperty("Content-Type", "application/octet-stream");
        System.out.println("done");
        System.out.flush();
        System.out.print("getting output stream...");
        ObjectOutputStream outputStreamToServlet = new ObjectOutputStream(new BufferedOutputStream(connectionToServlet.getOutputStream()));
        System.out.println("done");
        System.out.flush();
        System.out.print("writing fileSearch object...");
        outputStreamToServlet.writeObject((java.io.Serializable) fileSearch);
        outputStreamToServlet.flush();
        System.out.println("done");
        System.out.flush();
        System.out.print("closing output stream...");
        outputStreamToServlet.close();
        System.out.println("done");
        System.out.flush();
        System.out.print("setting up input stream...");
        ObjectInputStream inputStreamFromServlet = new ObjectInputStream(new BufferedInputStream(connectionToServlet.getInputStream()));
        System.out.println("done");
        System.out.flush();
        System.out.print("reading FileSearch object...");
        try {
            fileSearch = (FileSearch) inputStreamFromServlet.readObject();
        } catch (java.lang.ClassNotFoundException e) {
            throw new IOException("Error.  Servlet did not send back a response.");
        }
        System.out.println("done");
        System.out.flush();
        System.out.print("closing input stream...");
        inputStreamFromServlet.close();
        System.out.println("done");
        System.out.flush();
        return (fileSearch);
    }
