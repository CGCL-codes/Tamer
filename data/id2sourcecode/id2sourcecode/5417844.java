    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream out = response.getOutputStream();
        final String CONTENT_TYPE = "application/octet-stream; charset=ISO-8859-1";
        final String CONTENT_TYPE_TEXT = "text/html; charset=GB2312";
        log.showLog("----servlet--start---");
        String type = new String();
        String sqlwhere = new String();
        type = nullToString(request.getParameter("type"));
        if (!nullToString(request.getParameter("fileName")).equals("")) this.setDownFileName(request.getParameter("fileName"));
        String strRealPath = this.getServletContext().getRealPath("/");
        String strPath = new String();
        if (!type.equals("")) {
            sqlwhere = request.getParameter("sqlwhere");
            if (sqlwhere != null) sqlwhere = new String(replace(sqlwhere, "|", "%").getBytes("ISO8859-1"), "GB2312");
            sqlwhere = nullToString(sqlwhere);
            String strsql = getSql(type, sqlwhere);
            try {
                this.createDirtory(strRealPath);
                RsToFileUtil rs = new RsToFileUtil();
                strPath = rs.sqlRsToFile(strsql, strRealPath, type);
                java.io.File file = new java.io.File(strPath);
                java.io.FileInputStream fin = new java.io.FileInputStream(file);
                long len = file.length();
                Long lng = new Long(len);
                String strFile2 = new String(getDownFileName().getBytes(), "ISO-8859-1");
                response.addHeader("Content-Disposition", "attachment; filename=" + strFile2);
                response.addHeader("Content-Length", lng.toString());
                response.setContentType(CONTENT_TYPE);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                int b;
                while ((b = fin.read()) != -1) {
                    outStream.write(b);
                }
                out.write(outStream.toByteArray());
                out.flush();
                out.close();
                outStream.close();
                fin.close();
            } catch (DefaultException e) {
                response.setContentType(CONTENT_TYPE_TEXT);
                out.print("");
                out.print("Sorry, file not found!");
                out.print(" close ");
                e.printStackTrace(System.out);
            }
        } else {
            response.setContentType(CONTENT_TYPE_TEXT);
            out.print("");
            out.print("Sorry, file not found!");
            out.print(" close ");
        }
    }
