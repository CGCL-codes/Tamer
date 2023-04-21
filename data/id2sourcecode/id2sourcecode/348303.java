    public static void main(String[] args) throws Exception {
        int result = 20;
        if (args.length == 1) {
            StringBuffer urlString = new StringBuffer(args[0]);
            if (urlString.lastIndexOf("/") != urlString.length() - 1) {
                urlString.append('/');
            }
            urlString.append("GetFirmwareVersion.jsp");
            URLConnection conn = new URL(urlString.toString()).openConnection();
            System.out.println(FirmwareVersion.readObject(conn.getInputStream()));
            result = 0;
        } else {
            System.err.println("usage: GetFirmwareVersion <URL>");
        }
        System.exit(result);
    }
