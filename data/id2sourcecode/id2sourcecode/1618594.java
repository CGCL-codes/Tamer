    private void connect() throws IOException {
        URL url = new URL("http://" + host + "/" + name);
        if (name.equals("error")) {
            String error = Deploy.Client.toString(new Deploy.Client().send(url, null, null, true));
            if (error.indexOf("Error successful") == -1) {
                failed = true;
            }
        } else if (name.equals("never")) {
            ((HttpURLConnection) url.openConnection()).getResponseCode();
        } else {
            save(name, new Deploy.Client().send(url, file, null, true));
        }
    }
