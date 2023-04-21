    @Override
    protected void doGet() throws IOException {
        int streamID = Integer.parseInt(getParameter("id"));
        InputStream pngData = PngCache.PNG_CACHE.getInputStream(streamID);
        if (pngData == null) throw new TinyCGIException(HttpURLConnection.HTTP_NOT_FOUND, "Not Found", "Not Found");
        out.print("Content-type: image/png\r\n\r\n");
        out.flush();
        FileUtils.copyFile(pngData, outStream);
        outStream.flush();
        outStream.close();
    }
