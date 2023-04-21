    private void endRecording() throws Exception {
        oosRecording.flush();
        zosStream.finish();
        ScriptItemImpl[] items = script.getItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].getClassName().equals("com.sts.webmeet.content.server.slides.Server") && -1 != ((Integer) items[i].getItem()).intValue()) {
                ZipEntry entry = new ZipEntry(strConfID + "/" + (Integer) items[i].getItem());
                zosArchive.putNextEntry(entry);
                logger.info("trying to get image: " + (Integer) items[i].getItem());
                ImageDBUtil.writeImageData(zosArchive, ImageDBUtil.getImageFromDB((Integer) items[i].getItem()));
            }
        }
        InputStream is = null;
        ClassLoader loader = getClass().getClassLoader();
        String strZipRoot = strConfID + "/";
        for (int i = 0; i < PlaybackConstants.STATIC_FILES.length; i++) {
            is = loader.getResourceAsStream(PlaybackConstants.STATIC_FILES[i]);
            if (null != is) {
                writeStreamToZip(is, strZipRoot + PlaybackConstants.STATIC_FILES[i], zosArchive);
            }
        }
        Map map = new HashMap();
        map.put("product", System.getProperty("webhuddle.property.product", "WebHuddle"));
        for (int i = 0; i < PlaybackConstants.TEMPLATE_FILES.length; i++) {
            String strTranslated = FreeMarkerUtil.applyTemplate(PlaybackConstants.TEMPLATE_FILES[i], map);
            if (null != strTranslated) {
                writeStreamToZip(new ByteArrayInputStream(strTranslated.getBytes()), strZipRoot + FileUtil.replaceDotSuffix(PlaybackConstants.TEMPLATE_FILES[i], "html"), zosArchive);
            }
        }
        is = loader.getResourceAsStream(PlaybackConstants.PHP_LIST);
        writeStreamToZip(is, PlaybackConstants.PHP_LIST_OUT, zosArchive);
        is = new ByteArrayInputStream(confData.getName().getBytes());
        writeStreamToZip(is, strZipRoot + PlaybackConstants.NAME_FILE, zosArchive);
        is = new ByteArrayInputStream(confData.getDescription() != null ? confData.getDescription().getBytes() : "[no description]".getBytes());
        writeStreamToZip(is, strZipRoot + PlaybackConstants.DESCRIPTION_FILE, zosArchive);
        Properties recordingProps = new Properties();
        recordingProps.put(Constants.PLAYBACK_MEETING_LENGTH_PARAM, "" + this.lElapsedTimeMillis);
        ByteArrayOutputStream baosProps = new ByteArrayOutputStream();
        recordingProps.store(baosProps, null);
        baosProps.flush();
        is = new ByteArrayInputStream(baosProps.toByteArray());
        writeStreamToZip(is, strZipRoot + Constants.RECORDED_SESSION_INFO_PROPERTIES, zosArchive);
        zosArchive.close();
    }
