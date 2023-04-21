    public static void downloadImage(File imageFile, String imageURL) throws IOException {
        URL url = new URL(imageURL);
        URLConnection cnx = url.openConnection();
        cnx.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux x86_64; en-GB; rv:1.8.1.5) Gecko/20070719 Iceweasel/2.0.0.5 (Debian-2.0.0.5-0etch1)");
        FileTools.copy(cnx.getInputStream(), new FileOutputStream(imageFile));
    }
