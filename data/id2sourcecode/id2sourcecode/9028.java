    public static String xmlrpcNewMediaObject(String url, String login, String password, String filename, String mimeType, String file) throws Exception {
        url = url + "xmlrpc.php?";
        final XmlRpcClient xmlrpc = new XmlRpcClient(url);
        final Vector params = new Vector(0);
        final InputStream is = new FileInputStream(file);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        final BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
        final byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = bufferedInputStream.read(bytes)) > 0) {
            byteArrayOutputStream.write(bytes, 0, len);
        }
        is.close();
        bufferedInputStream.close();
        String blogID = "1";
        params.add(blogID);
        params.add(login);
        params.add(password);
        final Hashtable media = new Hashtable(0);
        media.put("name", filename);
        media.put("type", mimeType);
        media.put("bits", byteArrayOutputStream.toByteArray());
        params.add(media);
        final Hashtable response = (Hashtable) xmlrpc.execute("metaWeblog.newMediaObject", params);
        return ((String) response.get("url"));
    }
