    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        int i;
        String dicomURL = request.getParameter("datasetURL");
        String contentType = request.getParameter("contentType");
        String studyUID = request.getParameter("studyUID");
        String seriesUID = request.getParameter("seriesUID");
        String objectUID = request.getParameter("objectUID");
        dicomURL += "&contentType=" + contentType + "&studyUID=" + studyUID + "&seriesUID=" + seriesUID + "&objectUID=" + objectUID + "&transferSyntax=1.2.840.10008.1.2.1";
        dicomURL = dicomURL.replace("+", "%2B");
        InputStream is = null;
        DataInputStream dis = null;
        try {
            URL url = new URL(dicomURL);
            is = url.openStream();
            dis = new DataInputStream(is);
            for (i = 0; i < dicomData.length; i++) dicomData[i] = dis.readUnsignedByte();
            String noOfFrames = getElementValue("00280008").trim();
            String frameTime = getElementValue("00181063");
            String frameTimeVector = "";
            if (frameTime != "") {
                if (frameTime.indexOf(".") > 0) frameTime = frameTime.substring(0, frameTime.indexOf("."));
                for (int x = 0; x < Integer.parseInt(noOfFrames); x++) frameTimeVector = frameTimeVector + frameTime + ":";
            } else {
                frameTimeVector = getElementValue("00181065");
            }
            dis.skipBytes(50000000);
            is.close();
            dis.close();
            out.println(frameTimeVector);
            out.close();
        } catch (Exception e) {
            log.error("Unable to read multiframe dicom elements", e);
        }
    }
