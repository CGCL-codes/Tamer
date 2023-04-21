    public void download(File file, String uri) {
        URL url = null;
        String bts = "";
        try {
            file.createNewFile();
            url = new URL(uri);
            URLConnection conn = url.openConnection();
            is = conn.getInputStream();
            bar.setMaximum(conn.getContentLength());
            bts = "" + conn.getContentLength();
            fOut = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[32 * 1024];
            int bytesRead = 0;
            while ((bytesRead = is.read(buffer)) != -1) {
                bar.setValue(bar.getValue() + bytesRead);
                jLabel2.setText(bar.getValue() + " bytes of " + bts + " bytes");
                fOut.write(buffer, 0, bytesRead);
            }
            is.close();
            fOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
