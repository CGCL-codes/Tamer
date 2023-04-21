    public boolean decodeExtAudio(byte[] mpegData, int mpegFileSize, int mpegOffset) {
        if (dumpPmfFile) {
            try {
                new File(MediaEngine.getExtAudioBasePath(mpegFileSize)).mkdirs();
                FileOutputStream pmfOut = new FileOutputStream(MediaEngine.getExtAudioPath(mpegFileSize, "pmf"));
                pmfOut.write(mpegData);
                pmfOut.close();
            } catch (IOException e) {
                log.error(e);
            }
        }
        MpegDemux mpegDemux = new MpegDemux(mpegData, mpegOffset);
        try {
            mpegDemux.demux(false, true);
        } catch (OutOfMemoryError e) {
            log.error(String.format("Error '%s' while decoding external audio file (mpegFileSize=%d)", e.toString(), mpegFileSize));
            return false;
        }
        ByteBuffer audioStream = mpegDemux.getAudioStream();
        if (audioStream == null) {
            return false;
        }
        if (dumpAudioStreamFile) {
            try {
                new File(MediaEngine.getExtAudioBasePath(mpegFileSize)).mkdirs();
                FileOutputStream pmfOut = new FileOutputStream(MediaEngine.getExtAudioPath(mpegFileSize, "audio"));
                pmfOut.getChannel().write(audioStream);
                audioStream.rewind();
                pmfOut.close();
            } catch (IOException e) {
                log.error(e);
            }
        }
        ByteBuffer omaBuffer = OMAFormat.convertStreamToOMA(audioStream);
        if (omaBuffer == null) {
            return false;
        }
        try {
            new File(MediaEngine.getExtAudioBasePath(mpegFileSize)).mkdirs();
            String encodedFileName = MediaEngine.getExtAudioPath(mpegFileSize, "oma");
            FileOutputStream os = new FileOutputStream(encodedFileName);
            os.getChannel().write(omaBuffer);
            os.close();
            String decodedFileName = MediaEngine.getExtAudioPath(mpegFileSize, "wav");
            if (!executeExternalDecoder(encodedFileName, decodedFileName, keepOmaFile)) {
                return false;
            }
        } catch (IOException e) {
            log.error(e);
            return false;
        }
        return true;
    }
