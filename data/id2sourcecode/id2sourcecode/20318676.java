    private OcStorageResponse callWebScript(final OcStorageRequest sr) throws IOException, ClassNotFoundException {
        String alfrescoUrl = Singleton.getInstance().getProperty("alfresco-url");
        String webScriptUrl = Singleton.getInstance().getProperty("webScript-url");
        URL url = new URL(alfrescoUrl + webScriptUrl);
        URLConnection conn = url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setDefaultUseCaches(false);
        conn.setRequestProperty("Content-Type", "application/octet-stream");
        ObjectOutputStream outStream = new ObjectOutputStream(conn.getOutputStream());
        outStream.writeObject(sr);
        outStream.writeObject(configuration);
        outStream.flush();
        outStream.close();
        OcStorageResponse response = null;
        ObjectInputStream in = new ObjectInputStream(conn.getInputStream());
        response = (OcStorageResponse) in.readObject();
        outStream.close();
        return response;
    }
