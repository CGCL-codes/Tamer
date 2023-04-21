    public void connect() {
        File f = new File(dest);
        if (!f.exists()) {
            File f2 = new File(f.getParent());
            f2.mkdir();
        } else errorCode[FILE_EXISTS] = true;
        if (f.isDirectory()) {
            File f2 = new File(surl);
            f = new File(f.getAbsolutePath() + "/" + f2.getName());
            System.out.println("renamed to: " + f.getAbsolutePath());
            try {
                f.createNewFile();
            } catch (Exception e) {
                errorCode[NO_PERMISSION] = true;
            }
        }
        try {
            URL url = new URL(surl);
            out = new BufferedOutputStream(new FileOutputStream(f.getAbsolutePath()));
            urlc = url.openConnection();
            totalSize = urlc.getContentLength();
            in = urlc.getInputStream();
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
            errorCode[WRONG_URL_FORMAT] = true;
        } catch (UnknownHostException uhe) {
            errorCode[NO_HOST] = true;
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
            errorCode[NO_FILE] = true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
