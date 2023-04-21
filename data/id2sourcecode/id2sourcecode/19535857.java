    public File getJPEGThumnails(WadoParameters wadoParameters, String StudyUID, String SeriesUID, String SOPInstanceUID) throws Exception {
        URL url = new URL(wadoParameters.getWadoURL() + "?requestType=WADO&studyUID=" + StudyUID + "&seriesUID=" + SeriesUID + "&objectUID=" + SOPInstanceUID + "&contentType=image/jpeg&imageQuality=70" + "&rows=" + Thumbnail.MAX_SIZE + "&columns=" + Thumbnail.MAX_SIZE + wadoParameters.getAdditionnalParameters());
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoInput(true);
        httpCon.setRequestMethod("GET");
        if (wadoParameters.getWebLogin() != null) {
            httpCon.setRequestProperty("Authorization", "Basic " + wadoParameters.getWebLogin());
        }
        if (wadoParameters.getHttpTaglist().size() > 0) {
            for (HttpTag tag : wadoParameters.getHttpTaglist()) {
                httpCon.setRequestProperty(tag.getKey(), tag.getValue());
            }
        }
        httpCon.connect();
        if (httpCon.getResponseCode() / 100 != 2) {
            return null;
        }
        OutputStream out = null;
        InputStream in = null;
        File outFile = File.createTempFile("tumb_", ".jpg", AbstractProperties.APP_TEMP_DIR);
        try {
            out = new BufferedOutputStream(new FileOutputStream(outFile));
            in = httpCon.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtil.safeClose(in);
            FileUtil.safeClose(out);
        }
        return outFile;
    }
