    public void sendSplitedPDFFile(String currentSlide) {
        String nextSlide = Helper.putNextSlideName(currentSlide);
        log.trace("Now processing " + currentSlide + " and guessing next slide " + nextSlide);
        File currentFile = new File(fileSplitedPDFDir.getAbsolutePath() + System.getProperty("file.separator") + currentSlide);
        if (goOn(currentFile)) {
            try {
                log.trace("Current file I try to send: " + currentFile.getAbsolutePath());
                isFileIn = new FileInputStream(currentFile);
                while (isFileIn.available() > 0) {
                    dosPdfOutStream.write(byteBuffer, 0, isFileIn.read(byteBuffer));
                }
                isFileIn.close();
            } catch (FileNotFoundException e) {
                log.error("Could not find the given file. " + e.getMessage());
            } catch (IOException e) {
                log.error("Could not send the file: " + currentFile.getAbsolutePath() + currentFile.getName() + " " + e.getMessage());
            }
        }
    }
