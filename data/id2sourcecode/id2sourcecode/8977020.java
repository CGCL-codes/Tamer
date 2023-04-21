    private void downloadHTTPFile(String srcFile, String dstPath) {
        URL u;
        InputStream is = null;
        DataInputStream dis;
        String filename = srcFile.substring(srcFile.lastIndexOf("/") + 1, srcFile.length());
        try {
            u = new URL(srcFile);
            is = u.openStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            File f = new File(dstPath);
            f.mkdirs();
            f = new File(dstPath + "/" + filename);
            FileOutputStream fos = new FileOutputStream(f);
            byte[] tmpbuffer = new byte[1024];
            int readbytes = bis.read(tmpbuffer);
            while (readbytes > 0) {
                fos.write(tmpbuffer, 0, readbytes);
                readbytes = bis.read(tmpbuffer);
            }
            fos.close();
        } catch (MalformedURLException mue) {
            System.out.println("Ouch - a MalformedURLException happened.");
            mue.printStackTrace();
            System.exit(1);
        } catch (IOException ioe) {
            System.out.println("Oops- an IOException happened.");
            ioe.printStackTrace();
            System.exit(1);
        } finally {
            try {
                is.close();
            } catch (IOException ioe) {
                log.error("DataManagement.java: Error closing Input Stream.");
            }
        }
    }
