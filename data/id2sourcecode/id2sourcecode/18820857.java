    public Object invoke(String service, String method, Object... args) throws IOException {
        URL url = new URL(baseURL + "/ServiceRequest/" + service + "/" + method);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        connection.setRequestProperty("User-Agent", "JWSM Client");
        connection.setDoInput(true);
        if (args.length > 0) {
            connection.setDoOutput(true);
            ObjectOutputStream oos = new ObjectOutputStream(connection.getOutputStream());
            for (Object o : args) {
                oos.writeObject(o);
            }
            oos.flush();
        }
        System.out.println("Response: " + connection.getResponseCode() + "/" + connection.getResponseMessage());
        Object response = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());
            response = ois.readObject();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return response;
    }
