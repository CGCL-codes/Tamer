    public int deployRemoteWAR(final String urlPath, final String filename) {
        String message = null;
        URL url = null;
        System.out.print("path" + urlPath);
        int i = 1;
        try {
            url = new URL(urlPath);
            System.out.print("\n URL" + url.toString());
        } catch (MalformedURLException ex1) {
            message = ex1.toString() + "MalformedURLException";
            System.out.print(message);
            i = 0;
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException ex2) {
            message = ex2.toString() + "IOException";
            System.out.print(message);
            i = 2;
        }
        try {
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream outStream = new DataOutputStream(new FileOutputStream(new File("tmp/" + filename)));
            final int n = 1024;
            byte[] by = new byte[n];
            int m;
            while ((m = in.read(by)) != -1) {
                outStream.write(by, 0, m);
                outStream.flush();
            }
            in.close();
            outStream.close();
            System.out.print("success");
        } catch (IOException ex3) {
            message = ex3.toString() + " rein";
            i = C;
        }
        System.out.print("over");
        return i;
    }
