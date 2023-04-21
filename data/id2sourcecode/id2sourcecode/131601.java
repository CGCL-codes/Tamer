    private void sendRequest() {
        try {
            URL url = new URL(requestURL);
            URLConnection con = url.openConnection();
            con.setReadTimeout(timeout);
            checkStatus(getResponse(con));
        } catch (IOException e) {
            System.err.println("I/O Error while open connection");
            e.printStackTrace();
        }
    }
