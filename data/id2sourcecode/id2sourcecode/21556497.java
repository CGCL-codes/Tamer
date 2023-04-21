    @Override
    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            String sid = httpServletRequest.getParameter("sid");
            if (sid == null) {
                sid = "default";
            }
            log.debug("sid: " + sid);
            String hash = httpServletRequest.getParameter("hash");
            if (hash == null) {
                hash = "";
            }
            log.debug("hash: " + hash);
            String fileName = httpServletRequest.getParameter("fileName");
            if (fileName == null) {
                fileName = "file_xyz";
            }
            String exportType = httpServletRequest.getParameter("exportType");
            if (exportType == null) {
                exportType = "svg";
            }
            Long users_id = Sessionmanagement.getInstance().checkSession(sid);
            Long user_level = Usermanagement.getInstance().getUserLevelByID(users_id);
            log.debug("users_id: " + users_id);
            log.debug("user_level: " + user_level);
            if (user_level != null && user_level > 0 && hash != "") {
                PrintBean pBean = PrintService.getPrintItemByHash(hash);
                List whiteBoardMap = pBean.getMap();
                DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
                String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
                Document document = domImpl.createDocument(svgNS, "svg", null);
                Element svgRoot = document.getDocumentElement();
                svgRoot.setAttributeNS(null, "width", "" + pBean.getWidth());
                svgRoot.setAttributeNS(null, "height", "" + pBean.getHeight());
                log.debug("pBean.getWidth(),pBean.getHeight()" + pBean.getWidth() + "," + pBean.getHeight());
                SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
                svgGenerator = WhiteboardMapToSVG.getInstance().convertMapToSVG(svgGenerator, whiteBoardMap);
                boolean useCSS = true;
                String requestedFile = fileName + "_" + CalendarPatterns.getTimeForStreamId(new Date()) + ".svg";
                if (exportType.equals("svg")) {
                    Writer out = httpServletResponse.getWriter();
                    svgGenerator.stream(out, useCSS);
                } else if (exportType.equals("png") || exportType.equals("jpg") || exportType.equals("gif") || exportType.equals("tif") || exportType.equals("pdf")) {
                    String current_dir = getServletContext().getRealPath("/");
                    String working_dir = current_dir + "uploadtemp" + File.separatorChar;
                    String requestedFileSVG = fileName + "_" + CalendarPatterns.getTimeForStreamId(new Date()) + ".svg";
                    String resultFileName = fileName + "_" + CalendarPatterns.getTimeForStreamId(new Date()) + "." + exportType;
                    log.debug("current_dir: " + current_dir);
                    log.debug("working_dir: " + working_dir);
                    log.debug("requestedFileSVG: " + requestedFileSVG);
                    log.debug("resultFileName: " + resultFileName);
                    File svgFile = new File(working_dir + requestedFileSVG);
                    File resultFile = new File(working_dir + resultFileName);
                    log.debug("svgFile: " + svgFile.getAbsolutePath());
                    log.debug("resultFile: " + resultFile.getAbsolutePath());
                    log.debug("svgFile P: " + svgFile.getPath());
                    log.debug("resultFile P: " + resultFile.getPath());
                    FileWriter out = new FileWriter(svgFile);
                    svgGenerator.stream(out, useCSS);
                    HashMap<String, Object> returnError = GenerateImage.getInstance().convertImageByTypeAndSize(svgFile.getAbsolutePath(), resultFile.getAbsolutePath(), pBean.getWidth(), pBean.getHeight());
                    RandomAccessFile rf = new RandomAccessFile(resultFile.getAbsoluteFile(), "r");
                    httpServletResponse.reset();
                    httpServletResponse.resetBuffer();
                    OutputStream outStream = httpServletResponse.getOutputStream();
                    httpServletResponse.setContentType("APPLICATION/OCTET-STREAM");
                    httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + resultFileName + "\"");
                    httpServletResponse.setHeader("Content-Length", "" + rf.length());
                    byte[] buffer = new byte[1024];
                    int readed = -1;
                    while ((readed = rf.read(buffer, 0, buffer.length)) > -1) {
                        outStream.write(buffer, 0, readed);
                    }
                    rf.close();
                    out.flush();
                    out.close();
                }
            }
        } catch (Exception er) {
            log.error("ERROR ", er);
            System.out.println("Error exporting: " + er);
            er.printStackTrace();
        }
    }
