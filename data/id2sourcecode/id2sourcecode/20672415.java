    public void appletError(String error) {
        try {
            String message = MessageFormat.format(HttpFeedback.APPLET_ERROR_URL, new Object[] { URLEncoder.encode(this.customerID, "UTF-8"), URLEncoder.encode(error, "UTF-8") });
            URL url = new URL(message);
            URLConnection con = url.openConnection();
            con.connect();
            con.getHeaderField(0);
        } catch (MalformedURLException e) {
            System.out.println("Applet error " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Applet error " + e.getMessage());
        }
    }
