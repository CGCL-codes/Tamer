    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String temp;
        ServletOutputStream out = res.getOutputStream();
        String fileName = DateUtil.Format(DateUtil.DATA_FORMAT, new Date());
        res.setContentType("application/zip");
        res.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".zip;");
        if ((temp = req.getParameter("url")) != null) {
            ZipOutputStream zout = new ZipOutputStream(out);
            HttpURLConnection con = null;
            URL url;
            try {
                url = new URL(temp);
                con = (HttpURLConnection) url.openConnection();
                DataInputStream input = new DataInputStream(con.getInputStream());
                zout.setMethod(ZipOutputStream.DEFLATED);
                zout.putNextEntry(new ZipEntry(fileName + ".html"));
                byte[] data = new byte[1024];
                int nbRead = 0;
                while (nbRead >= 0) {
                    try {
                        nbRead = input.read(data);
                        if (nbRead >= 0) zout.write(data, 0, nbRead);
                    } catch (Exception e) {
                        nbRead = -1;
                    }
                }
                zout.closeEntry();
                out.flush();
            } catch (Exception e) {
                res.setContentType("text/html");
                out.println("<html><head><title>Error</title></head>");
                out.println("<body><b>");
                out.println("An error has occured while processing " + temp + "<br>");
                out.println("Here is the exception: <br>" + e + "<br>");
                e.printStackTrace(new PrintWriter(out));
                out.println("</body>");
                out.println("</html>");
            }
            if (zout != null) {
                try {
                    zout.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
