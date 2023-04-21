    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requested = request.getRequestURI();
        String servletPath = request.getServletPath();
        int index = requested.indexOf(servletPath);
        String requestedImage = requested.substring(index + servletPath.length());
        requestedImage = URLDecoder.decode(requestedImage, "ISO-8859-1");
        if (requestedImage.startsWith("/")) {
            requestedImage = requestedImage.substring(1);
        }
        logger.info("Requested image = " + requestedImage);
        File image = new File(parentFolder, requestedImage);
        if (!image.exists()) {
            logger.error("Requested image '" + requestedImage + "' was not found.");
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            response.setContentType("image/jpeg");
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(image);
            int read;
            byte[] buff = new byte[1024];
            while ((read = fis.read(buff)) > 0) {
                os.write(buff, 0, read);
            }
            os.flush();
            fis.close();
        }
    }
