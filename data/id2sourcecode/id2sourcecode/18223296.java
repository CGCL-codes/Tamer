    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        String mp4Location = (String) request.getParameter("bslink");
        System.out.println("mp4" + mp4Location);
        String colName = (String) request.getSession().getAttribute("colname");
        System.out.println("colName" + colName);
        String titleString = (String) request.getSession().getAttribute("titlestring");
        System.out.println("titleString" + titleString);
        String authorString = (String) request.getSession().getAttribute("authorstring");
        System.out.println("authorString" + authorString);
        String levelString = (String) request.getSession().getAttribute("levelstring");
        System.out.println("levelString" + levelString);
        String timeString = (String) request.getSession().getAttribute("levelstring");
        System.out.println("timeString" + timeString);
        String template = "<script type=\"text/javascript\" src=\"/dspace/flvtemplate/swfobject.js\">" + "</script>" + "<div align=\"center\">" + "<h2>" + colName + "</h2>" + "<table cellpadding=\"0\" cellspacing=\"0\"><tr><td colspan=\"2\">" + "<p id=\"player1\">" + "กรุณา download <a href=\"http://www.macromedia.com/go/getflashplayer\">Get the Flash Player</a> ในกรณีที่ไม่สามารถดู clip ได้</p>" + "<script type=\"text/javascript\">	var s1 = new SWFObject(\"/dspace/flvtemplate/flvplayer.swf\",\"single\",\"352\",\"288\",\"7\");" + "s1.addParam(\"allowfullscreen\",\"true\");" + "s1.addVariable(\"file\",\"" + mp4Location + "\");" + "s1.addVariable(\"image\",\"preview.jpg\");" + "s1.write(\"player1\");" + "</script>" + "</td></tr>" + "<tr><td width=\"62\"  bgcolor=\"#FFCC00\">" + "<b>ชื่อเรื่อง : </b>" + "</td>" + "<td width=\"290\"  bgcolor=\"#FFCC00\">" + titleString + "</td></tr>" + "<tr><td width=\"62\"  bgcolor=\"#FFCC00\">" + "<b>ผู้แต่ง : </b>" + "</td>" + "<td width=\"290\"  bgcolor=\"#FFCC00\">" + authorString + "</td></tr>" + "<tr><td width=\"62\"  bgcolor=\"#FFCC00\">" + "<b>ระดับ : </b>" + "</td>" + "<td width=\"290\"  bgcolor=\"#FFCC00\">" + levelString + "</td></tr>" + "<tr><td width=\"62\"  bgcolor=\"#FFCC00\">" + "<b>เวลา : </b>" + "</td>" + "<td width=\"290\"  bgcolor=\"#FFCC00\">" + timeString + "</td></tr>" + "</table>" + "</div>";
        String all = "";
        File workPlaybackTemplateFile = null;
        try {
            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int serverPort = request.getServerPort();
            String contextPath = request.getContextPath();
            String videoTemplateFile = scheme + "://" + serverName + ":" + serverPort + contextPath + "/flvtemplate/viewvideotemplate.jsp";
            String address = videoTemplateFile;
            OutputStream out2 = null;
            URLConnection conn = null;
            InputStream in = null;
            try {
                URL url = new URL(address);
                log.debug("Creating temp playbackTemplateFile...");
                File tempDir = (File) this.getServletContext().getAttribute("javax.servlet.context.tempdir");
                workPlaybackTemplateFile = File.createTempFile(PLAYBACK_TEMPLATE_FILENAME, PLAYBACK_TEMPLATE_FILE_EXTENSION, tempDir);
                log.debug("playbackTemplateFile " + workPlaybackTemplateFile.getAbsolutePath() + " created.");
                out2 = new BufferedOutputStream(new FileOutputStream(workPlaybackTemplateFile));
                conn = url.openConnection();
                in = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int numRead;
                long numWritten = 0;
                while ((numRead = in.read(buffer)) != -1) {
                    out2.write(buffer, 0, numRead);
                    numWritten += numRead;
                }
                log.debug(workPlaybackTemplateFile.getName() + "\t" + numWritten);
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out2 != null) {
                        out2.close();
                    }
                } catch (IOException ioe) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file = workPlaybackTemplateFile;
        InputStream is = new FileInputStream(file);
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        all = new String(bytes, "UTF-8");
        template = all.replaceAll("viewvideocontent", template);
        out.write(template);
        out.close();
        workPlaybackTemplateFile.delete();
    }
