    public static void viewFile(HttpServletResponse response, String where, String serverSubPath, String physicalName, String mimeTypeParam) throws Exception {
        String mimeType = mimeTypeParam;
        String downFileName = where + SEPERATOR + serverSubPath + SEPERATOR + physicalName;
        File file = new File(EgovWebUtil.filePathBlackList(downFileName));
        if (!file.exists()) {
            throw new FileNotFoundException(downFileName);
        }
        if (!file.isFile()) {
            throw new FileNotFoundException(downFileName);
        }
        byte[] b = new byte[BUFFER_SIZE];
        if (mimeType == null) {
            mimeType = "application/octet-stream;";
        }
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", "filename=image;");
        BufferedInputStream fin = null;
        BufferedOutputStream outs = null;
        try {
            fin = new BufferedInputStream(new FileInputStream(file));
            outs = new BufferedOutputStream(response.getOutputStream());
            int read = 0;
            while ((read = fin.read(b)) != -1) {
                outs.write(b, 0, read);
            }
        } finally {
            if (outs != null) {
                try {
                    outs.close();
                } catch (Exception ignore) {
                    System.out.println("IGNORE: " + ignore);
                }
            }
            if (fin != null) {
                try {
                    fin.close();
                } catch (Exception ignore) {
                    System.out.println("IGNORE: " + ignore);
                }
            }
        }
    }
