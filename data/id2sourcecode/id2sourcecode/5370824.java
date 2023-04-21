    public boolean downloadFile(String serverFile, String clientFile) {
        boolean rvalue = false;
        try {
            String urlString = dms_url + "/datafiles/" + serverFile;
            URL u = new URL(urlString);
            DataInputStream is = new DataInputStream(u.openStream());
            System.out.println(urlString);
            FileOutputStream os = new FileOutputStream(clientFile);
            int iBufSize = is.available();
            byte inBuf[] = new byte[20000 * 1024];
            int iNumRead;
            while ((iNumRead = is.read(inBuf, 0, iBufSize)) > 0) os.write(inBuf, 0, iNumRead);
            os.close();
            is.close();
            rvalue = true;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return rvalue;
    }
