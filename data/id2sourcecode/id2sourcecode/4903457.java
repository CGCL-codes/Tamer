    public static void downFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String downFileName = EgovStringUtil.isNullToString(request.getAttribute("downFile")).replaceAll("..", "");
        String orgFileName = EgovStringUtil.isNullToString(request.getAttribute("orgFileName")).replaceAll("..", "");
        File file = new File(downFileName);
        if (!file.exists()) {
            throw new FileNotFoundException(downFileName);
        }
        if (!file.isFile()) {
            throw new FileNotFoundException(downFileName);
        }
        byte[] b = new byte[BUFF_SIZE];
        String fName = (new String(orgFileName.getBytes(), "UTF-8")).replaceAll("\r\n", "");
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition:", "attachment; filename=" + fName);
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
                try {
                    outs.close();
                } catch (Exception ignore) {
                    LOG.debug("IGNORED: " + ignore.getMessage());
                }
            }
            if (fin != null) {
                try {
                    fin.close();
                } catch (Exception ignore) {
                    LOG.debug("IGNORED: " + ignore.getMessage());
                }
            }
        }
    }
