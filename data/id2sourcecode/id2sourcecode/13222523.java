    public void downloadFile(HttpServletResponse response, HttpServletRequest request, String path, String phyName, String name) throws Exception {
        String downFileName = properties.getProperty("baseDir") + SEPERATOR + path + SEPERATOR + phyName;
        downFileName = downFileName.replaceAll("\\\\", "/");
        File file = new File(downFileName);
        if (!file.exists()) {
            throw new FileNotFoundException(downFileName);
        }
        if (!file.isFile()) {
            throw new FileNotFoundException(downFileName);
        }
        byte[] b = new byte[BUFFER_SIZE];
        response.setContentType("application/octet-stream");
        response.setContentType("application/x-download");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.indexOf("MSIE 5.5") > -1) {
            response.setHeader("Content-Disposition", "filename=" + URLEncoder.encode(name.replaceAll(" ", "_"), CHAR_SET) + ";");
        } else if (userAgent.indexOf("MSIE") > -1) {
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(name.replaceAll(" ", "_"), CHAR_SET) + ";");
        } else {
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(name.replaceAll(" ", "_").getBytes(CHAR_SET), "latin1") + ";");
        }
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
