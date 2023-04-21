    public static void main(String[] args) {
        try {
            String passwd = String.valueOf(Math.random());
            if (args.length > 0) {
                passwd = args[0];
            }
            String hash = "" + passwd.hashCode();
            OutputStream os = new FileOutputStream(".admin");
            os.write(hash.getBytes());
            os.flush();
            os.close();
            String host = System.getProperty("host", "localhost");
            int port = Integer.getInteger("port", 8080).intValue();
            URL url = new URL("http", host, port, "/admin?cmd=shutdown&password=" + passwd);
            InputStream is = url.openStream();
            int c;
            StringBuffer sb = new StringBuffer();
            while ((c = is.read()) != -1) {
                sb.append((char) c);
            }
            Logger.getInstance().log(sb.toString());
        } catch (Throwable e) {
            Logger.getInstance().log(e.toString());
        }
    }
