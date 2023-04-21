    private void downloadImage(WebImage image) {
        URL url;
        InputStream is = null;
        DataInputStream dis = null;
        FileOutputStream fos = null;
        String strFileName = String.format("%s%s%s_%s.jpg", this.mstrTargetFolder, File.separator, this.mstrCacheCode, image.mstrName);
        try {
            fos = new FileOutputStream(strFileName);
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(fos));
            url = new URL(image.mstrURL);
            is = url.openStream();
            dis = new DataInputStream(new BufferedInputStream(is));
            byte[] buf = new byte[1024];
            int numRead = 0;
            while ((numRead = dis.read(buf)) != -1) {
                dos.write(buf, 0, numRead);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
