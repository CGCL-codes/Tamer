    private void secureRedirect() throws IOException {
        URL url = new URL(con.getHeaderField("Location"));
        out.println("Secure Redirect to: " + url);
        con = (HttpsURLConnection) url.openConnection();
    }
