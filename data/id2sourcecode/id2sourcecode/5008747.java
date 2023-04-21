    public static void main(String[] args) {
        System.err.println("Started 2...");
        setProxy("127.0.0.1", 8888);
        try {
            URL url = new URL("http://www.concord.org/");
            URLConnection openConnection = url.openConnection();
            InputStream inputStream = openConnection.getInputStream();
            inputStream.read();
            inputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.setProperty("http.proxyHost", "127.0.0.1");
            System.setProperty("http.proxyPort", "8888");
            URL url = new URL("http://www.concord.org/");
            URLConnection openConnection = url.openConnection();
            InputStream inputStream = openConnection.getInputStream();
            inputStream.read();
            inputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showConfirmDialog(null, "First Part Complete");
        loadPart("second");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JOptionPane.showConfirmDialog(null, "Second Part Complete");
    }
