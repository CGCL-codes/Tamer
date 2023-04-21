    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = URLDecoder.decode(request.getRequestURI());
        server.printlnDebug("ServletSendFile.service: uri=" + uri);
        String fileName = server.getServerRoot() + uri;
        server.printlnDebug("ServletSendFile.service: filename=" + fileName);
        File root = new File(server.getServerRoot());
        File f = new File(fileName);
        String serverRootPath = root.getCanonicalPath();
        String filePath = f.getCanonicalPath();
        if (!filePath.startsWith(serverRootPath)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (!f.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (!f.canRead()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (f.isDirectory()) {
            if (uri.charAt(uri.length() - 1) != '/') {
                response.sendRedirect("http://" + server.getServerName() + (server.getServerPort() == 80 ? "" : ":" + server.getServerPort()) + uri + "/");
                return;
            }
        }
        File defaultFile = new File(fileName + "/" + server.getDefaultDocuments()[0]);
        if (f.isDirectory() && !defaultFile.exists()) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">");
            out.println("<HTML>");
            out.println("<HEAD>");
            out.println("\t<TITLE>Index of " + uri + "</TITLE>");
            out.println("</HEAD>");
            out.println("<BODY>");
            out.println("<H1>Index of " + uri + "</H1>");
            out.println("<PRE><IMG SRC=\"/icons/blank.gif\" ALT=\"     \"> <A HREF=\"#\">Name</A>                    <A HREF=\"#\">Last modified</A>       <A HREF=\"#\">Size</A>  <A HREF=\"#\">Description</A>");
            out.println("<HR>");
            String virtualPath = uri.substring(0, uri.length() - 1);
            int spos = virtualPath.lastIndexOf("/");
            if (spos != -1) out.println("<IMG SRC=\"/icons/folder.gif\" ALT=\"[DIR]\"> <A HREF=\"" + virtualPath.substring(0, spos) + "/\">Parent Directory</A>        29-Oct-2001 11:21      -  ");
            File[] list = f.listFiles();
            for (int i = 0; i < list.length; i++) {
                File file = list[i];
                if (file.isDirectory()) {
                    String dirName = file.getName();
                    StringBuffer dirB = new StringBuffer(" ");
                    if (dirName.length() > 23) dirName = dirName.substring(0, 20) + "..&gt;"; else for (int j = dirName.length(); j < 23; j++) dirB.append(" ");
                    Date dirDate = new Date(file.lastModified());
                    out.println("<IMG SRC=\"/icons/folder.gif\" ALT=\"[DIR]\"> <A HREF=\"" + file.getName() + "/\">" + dirName + "</A>" + dirB.toString() + sdf.format(dirDate) + "      -  ");
                }
            }
            for (int i = 0; i < list.length; i++) {
                File file = list[i];
                if (!file.isDirectory()) {
                    String currentFileName = file.getName();
                    StringBuffer fileB = new StringBuffer(" ");
                    if (currentFileName.length() > 23) currentFileName = currentFileName.substring(0, 20) + "..&gt;"; else for (int j = currentFileName.length(); j < 23; j++) fileB.append(" ");
                    Date fileDate = new Date(file.lastModified());
                    long fileSize = 1 + Math.round(Math.floor((double) file.length() / 1024));
                    if (fileSize > 1024) {
                        double longFileSize = Math.round((double) fileSize / 102.4) / (double) 10;
                        String sFileSize = "" + longFileSize + "M";
                        StringBuffer fileS = new StringBuffer(" ");
                        for (int j = sFileSize.length(); j < 5; j++) fileS.append(" ");
                        out.println("<IMG SRC=\"/icons/text.gif\" ALT=\"[TXT]\"> <A HREF=\"" + file.getName() + "\">" + currentFileName + "</A>" + fileB.toString() + sdf.format(fileDate) + " " + fileS.toString() + sFileSize + "  ");
                    } else {
                        String sFileSize = "" + fileSize + "k";
                        StringBuffer fileS = new StringBuffer(" ");
                        for (int j = sFileSize.length(); j < 5; j++) fileS.append(" ");
                        out.println("<IMG SRC=\"/icons/text.gif\" ALT=\"[TXT]\"> <A HREF=\"" + file.getName() + "\">" + currentFileName + "</A>" + fileB.toString() + sdf.format(fileDate) + " " + fileS.toString() + sFileSize + "  ");
                    }
                }
            }
            out.println("</PRE><HR>");
            out.println("<ADDRESS>" + getServletContext().getServerInfo() + " Server at " + server.getServerName() + " Port " + server.getServerPort() + "</ADDRESS>");
            out.println("</BODY></HTML>");
            return;
        }
        if (f.isDirectory() && defaultFile.exists()) {
            fileName = fileName + "/" + server.getDefaultDocuments()[0];
            f = defaultFile;
        }
        response.setContentType(getServletContext().getMimeType(fileName));
        response.setContentLength((int) f.length());
        response.addDateHeader("Last-Modified", f.lastModified());
        FileInputStream fis = new FileInputStream(f);
        BufferedInputStream bis = new BufferedInputStream(fis);
        try {
            String encodings = request.getHeader("Accept-Encoding");
            if ((server.isGzipEnabled()) && (encodings != null) && (encodings.indexOf("gzip") != -1) && (f.length() < 65536)) {
                response.setHeader("Content-Encoding", "gzip");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                GZIPOutputStream gzos = new GZIPOutputStream(baos);
                byte[] buffer = new byte[65536];
                boolean cont = true;
                while (cont) {
                    int read = bis.read(buffer, 0, 65536);
                    if (read == -1) cont = false; else {
                        gzos.write(buffer, 0, read);
                    }
                }
                gzos.close();
                byte[] compressedStream = baos.toByteArray();
                response.setContentLength(compressedStream.length);
                ServletOutputStream sos = response.getOutputStream();
                sos.write(compressedStream);
            } else {
                ServletOutputStream sos = response.getOutputStream();
                byte[] buffer = new byte[65536];
                boolean cont = true;
                while (cont) {
                    int read = bis.read(buffer, 0, 65536);
                    if (read == -1) cont = false; else {
                        sos.write(buffer, 0, read);
                    }
                }
            }
        } catch (IOException e) {
        } finally {
            bis.close();
        }
    }
