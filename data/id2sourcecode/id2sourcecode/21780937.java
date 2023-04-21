    private boolean initRecording() throws Exception {
        fileRecordingDir = new File(com.sts.webmeet.server.util.Recordings.getRecordingDir(strConfID));
        fileRecordingDir.mkdirs();
        zosArchive = new ZipOutputStream(new FileOutputStream(fileRecordingDir.getAbsolutePath() + "/" + com.sts.webmeet.server.util.Recordings.getArchiveForConf(strConfID)));
        zosArchive.putNextEntry(new ZipEntry(strConfID + "/" + PlaybackConstants.STREAM_FILE_ARCHIVE));
        zosStream = new ZipOutputStream(zosArchive);
        zosStream.putNextEntry(new ZipEntry(PlaybackConstants.STREAM_FILE));
        oosRecording = new ObjectOutputStream(zosStream);
        dateStart = new Date();
        logger.info("recording inited (" + fileRecordingDir.getAbsolutePath() + ")");
        return true;
    }
