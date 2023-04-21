    public void joinVideos(JoinVideosInfo joinVideosInfo) {
        try {
            String[] inputVideos = joinVideosInfo.getInputVideos();
            File tempFile = File.createTempFile("iriverter-", ".avi");
            tempFile.deleteOnExit();
            Logger.logMessage("Join Videos: " + tempFile, Logger.INFO);
            progressDialogInfo.setOutputVideo(tempFile.getName());
            progressDialogInfo.setStatus("Concatenating videos to a temporary file...");
            FileOutputStream out = new FileOutputStream(tempFile);
            SequenceInputStream in = new SequenceInputStream(new ListOfFiles(inputVideos, progressDialogInfo));
            byte[] bytes = new byte[4096];
            int length;
            while ((length = in.read(bytes)) != -1 && !isCanceled) out.write(bytes, 0, length);
            progressDialogInfo.setPercentComplete(100);
            if (!isCanceled) {
                Logger.logMessage("Writing header...", Logger.INFO);
                progressDialogInfo.setInputVideo(tempFile.getName());
                progressDialogInfo.setOutputVideo(new File(joinVideosInfo.getOutputVideo()).getName());
                progressDialogInfo.setStatus("Writing header");
                splitVideo(joinVideosInfo.getOutputVideo(), runConversionCommand(new String[] { MPlayerInfo.getMEncoderExecutable(), "-forceidx", tempFile.toString(), "-o", joinVideosInfo.getOutputVideo(), "-ovc", "copy", "-oac", "copy" }));
            }
        } catch (IOException e) {
        }
    }
