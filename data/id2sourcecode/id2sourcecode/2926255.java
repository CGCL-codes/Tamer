    public static void downloadFile(HttpServletResponse response, String where, String serverSubPath, String physicalName, String original) throws Exception {
        String downFileName = where + SEPERATOR + serverSubPath + SEPERATOR + physicalName;
        File file = new File(EgovWebUtil.filePathBlackList(downFileName));
        if (!file.exists()) {
            throw new FileNotFoundException(downFileName);
        }
        if (!file.isFile()) {
            throw new FileNotFoundException(downFileName);
        }
        byte[] b = new byte[BUFFER_SIZE];
        original = original.replaceAll("\r", "").replaceAll("\n", "");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + convert(original) + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
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
                outs.close();
            }
            if (fin != null) {
                fin.close();
            }
        }
    }
