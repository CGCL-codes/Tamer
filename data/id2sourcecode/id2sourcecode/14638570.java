    private boolean canExiftoolBeExecuted() {
        ExifToolsFeedback exifToolFeedbackTest = new ExifToolsFeedback();
        String command = "exiftool -ver";
        ExiftoolCaller testCaller = new ExiftoolCaller(exifToolFeedbackTest);
        testCaller.setCommand(command);
        boolean canExecute = testCaller.canExecuteCommand(command);
        if (canExecute) {
            this.guiFeedback.receiveFeedback("Exiftool can be used.");
            this.logger.fine("Yes, can execute exiftool.");
        } else {
            this.guiFeedback.receiveFeedback("Exiftool-Error: Exiftool can not read / write pictures. \nReason: Exiftool can not be found and/or used.\nExample: Under Windows the file 'exiftool.exe' might be missing.");
            this.logger.fine("No, can not execute exiftool.");
        }
        return canExecute;
    }
