    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String videoUrl = request.getParameter("videoUrl");
        videoUrl = Base64Util.decodeS(videoUrl);
        LOG.info("movie download request: " + videoUrl);
        List<Video> list = VideoProvider.getVideos(videoUrl);
        if (list == null || list.size() == 0) {
            throw new IllegalArgumentException("could not find video: " + videoUrl);
        }
        if (list.size() > 1) {
            throw new IllegalArgumentException("found more than one video... " + videoUrl);
        }
        String filePath = VideoProvider.fetchVideo(videoUrl);
        response.setContentType(list.get(0).getVidType());
        response.setHeader("Content-Length", String.valueOf(new File(filePath).length()));
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            ServletOutputStream out = response.getOutputStream();
            byte[] buf = new byte[5048];
            int read;
            while ((read = inputStream.read(buf)) > 0) {
                out.write(buf, 0, read);
            }
            out.flush();
            inputStream.close();
            VideoProvider.cleanupAfterFetch(videoUrl);
            LOG.info("movie download request complete: " + videoUrl);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
