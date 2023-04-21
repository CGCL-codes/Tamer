    public static void main(String[] args) throws IOException {
        String host = null, dir = null, username = null, password = null, filename = null, urlStr = null, type = null;
        if (args.length == 6) {
            host = args[0];
            dir = args[1];
            username = args[2];
            password = args[3];
            filename = args[4];
            type = args[5];
            urlStr = "ftp://" + username + ":" + password + "@" + host + "/" + dir + ";type=" + (type.equals("bin") ? "i" : "a");
        } else {
            System.out.println("Usage: quickFTP <host> <dir> <username> <password> <file> <bin|asc>");
            urlStr = "ftp://seifertmail.com:vGjgwGgM@ftp.seifertmail.com/muhcows/sqlpro.jar;type=i";
            filename = "build/sqlpro.jar";
            System.exit(0);
        }
        System.out.println("Connecting to " + urlStr);
        URL url = new URL(urlStr);
        URLConnection urlc = url.openConnection();
        OutputStream target = urlc.getOutputStream();
        InputStream source = new FileInputStream(filename);
        byte buf[] = new byte[1024];
        int res = 0, bytes = 0;
        while ((res = source.read(buf)) > 0) {
            target.write(buf, 0, res);
            bytes += res;
        }
        System.out.println("\nTransferred " + filename + " successfully (" + bytes + " bytes)");
        source.close();
        target.close();
    }
