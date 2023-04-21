    public static File downloadFile(URL url, String name) throws IOException, ConnectException, UnknownHostException {
        File f = null;
        try {
            if ((f = getPreviousDownloadedURL(url)) == null) {
                File tempDirectory = new File(TEMPDIRECTORYPATH);
                if (!tempDirectory.exists()) tempDirectory.mkdir();
                f = new File(TEMPDIRECTORYPATH + "/" + name + System.currentTimeMillis());
                System.out.println("downloading '" + url.toString() + "' to: " + f.getAbsolutePath());
                f.deleteOnExit();
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
                byte[] buffer = new byte[1024 * 256];
                InputStream is = url.openStream();
                long readed = 0;
                for (int i = is.read(buffer); i > 0; i = is.read(buffer)) {
                    dos.write(buffer, 0, i);
                    readed += i;
                }
                dos.close();
                addDownloadedURL(url, f.getAbsolutePath());
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
        return f;
    }
