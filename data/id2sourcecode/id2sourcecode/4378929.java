    public void connect(String link) {
        try {
            url = new URL(link);
            conn = url.openConnection();
            inputStream = conn.getInputStream();
            isOnline = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            isOnline = false;
        }
    }
