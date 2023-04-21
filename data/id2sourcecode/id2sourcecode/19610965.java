    private boolean loadPrintData() {
        try {
            URL url = new URL(_dataURL);
            System.out.println("Connecting to :" + _dataURL);
            URLConnection conn = url.openConnection();
            _dataContentType = conn.getContentType();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            System.out.println("Reading ......");
            byte[] buf = new byte[1024];
            int len;
            while ((len = bis.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            bis.close();
            bos.flush();
            bos.close();
            System.out.println("Reading completed successfully");
            _printData = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
