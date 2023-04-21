    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean foundChart = false;
        String doi = request.getParameter("doi");
        if (doi != null && repID != null) {
            String relPath = FileNamingUtils.getDirectoryFromDOI(doi);
            relPath += "citechart.png";
            String path = repMap.buildFilePath(repID, relPath);
            File chartFile = new File(path);
            if (!chartFile.exists()) {
                RedirectUtils.sendRedirect(request, response, "/images/nochart.png");
                return;
            }
            response.reset();
            response.setContentType("image/png");
            FileInputStream in = new FileInputStream(chartFile);
            BufferedInputStream input = new BufferedInputStream(in);
            int contentLength = input.available();
            response.setContentLength(contentLength);
            BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream());
            while (contentLength-- > 0) {
                output.write(input.read());
            }
            output.flush();
        } else {
            RedirectUtils.sendRedirect(request, response, "/images/nochart.png");
        }
    }
