    private void convertMedia(String[] command) throws FFMpegException {
        LOGGER.info("Conversion command = " + Arrays.asList(command));
        ProcessBuilder builder = new ProcessBuilder(command);
        Process ffmpeg = null;
        try {
            ffmpeg = builder.start();
            Thread.sleep(100);
        } catch (IOException e) {
            throw new FFMpegException("Can't start FFMpeg", e);
        } catch (InterruptedException e) {
            throw new FFMpegException("Can't start FFMpeg", e);
        }
        try {
            ffmpeg.getOutputStream().close();
            ffmpeg.getInputStream().close();
            ffmpeg.getErrorStream().close();
        } catch (IOException e) {
            throw new FFMpegException("Can't start FFMpeg without output or error stream", e);
        }
        int exitStatus = -1;
        try {
            exitStatus = ffmpeg.waitFor();
        } catch (InterruptedException e) {
            throw new FFMpegException("FFMpeg conversion interrupted", e);
        }
        if (exitStatus != 0) {
            throw new FFMpegException(new StringBuilder("FFMpeg conversion failed with status = [").append(exitStatus).append("] using command = ").append(Arrays.asList(command)).toString());
        }
    }
