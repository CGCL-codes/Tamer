    public static void main(String[] args) {
        try {
            Authenticator a = new StdioAuthenticator();
            Authenticator.setDefault(a);
            URL url = new URL(args[0]);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            if (is != null) {
                System.out.println("URL opened OK.");
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
