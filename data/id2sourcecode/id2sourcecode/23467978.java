    public static WGETJavaResults DownloadFile(URL theURL) throws IOException {
        URLConnection con;
        UID uid = new UID();
        con = theURL.openConnection();
        con.connect();
        String type = con.getContentType();
        System.out.println(type);
        if (type != null) {
            byte[] buffer = new byte[4 * 1024];
            int read;
            String[] split = type.split("/");
            String theFile = Integer.toHexString(uid.hashCode()) + "_" + split[split.length - 1];
            FileOutputStream os = new FileOutputStream(theFile);
            InputStream in = con.getInputStream();
            while ((read = in.read(buffer)) > 0) {
                os.write(buffer, 0, read);
            }
            os.close();
            in.close();
            return WGETJavaResults.COMPLETE;
        } else {
            return WGETJavaResults.FAILED_UKNOWNTYPE;
        }
    }
