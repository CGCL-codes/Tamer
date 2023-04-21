    private String getSessionID() {
        String id = null;
        try {
            URL url = new URL("http://plikojad.pl/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String headerField = conn.getHeaderField("Set-Cookie");
            id = headerField.substring(headerField.indexOf('=') + 1, headerField.indexOf(';'));
        } catch (IOException ex) {
            Logger.getLogger(Plikojad.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return id;
        }
    }
