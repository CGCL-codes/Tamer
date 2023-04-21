    @SuppressWarnings("rawtypes")
    public List getGenericList(URL url) {
        List filesList = null;
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Content-Type", "text/xml");
            httpConnection.addRequestProperty("Cookie", session);
            InputStream in = httpConnection.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(in);
            try {
                filesList = (List) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filesList;
    }
