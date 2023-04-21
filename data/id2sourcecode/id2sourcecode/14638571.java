    private boolean isExiftoolReactingToArgsFile() throws ExiftoolTimeoutException {
        this.exifToolFeedback = new ExifToolsFeedback();
        String command = "exiftool -stay_open True -@ " + exifToolArgFile;
        this.logger.log(Level.INFO, "Starting exiftool and keep it open to read the args file using command ''{0}''...", command);
        if (this.caller == null) {
            this.caller = new ExiftoolCaller(this.exifToolFeedback);
        }
        caller.setCommand(command);
        caller.start();
        String args = "-ver\n-execute\n";
        this.logger.fine("Testing if exiftool responds to messages written into the args file...");
        String versionString = this.writeArgsToFileAndWait(args, null, false);
        if (versionString == null) {
            this.guiFeedback.receiveFeedback("Can not read/write pictures. Reason: Exiftool does not respond to messages written to file '" + this.exifToolArgFile + "'");
            return false;
        }
        boolean isVersionString = versionString.matches("\\d+\\.\\d+");
        if (isVersionString) {
            this.guiFeedback.receiveFeedback("Exiftool version is: " + versionString);
            this.logger.log(Level.FINE, "Yes, exiftool seems to respond to arguments written to the args file ''{0}''. Exiftool replied its version ''{1}''.", new Object[] { this.exifToolArgFile, versionString });
        } else {
            this.guiFeedback.receiveFeedback("Can not read/write pictures. Reason: Exiftool responds to messages written to file '" + this.exifToolArgFile + "' but the respons '" + versionString + " seems to be not a version.");
            this.logger.log(Level.WARNING, "No, exiftool does not seem to respond to arguments written to the args file ''{0}''. Exiftool was asked for its version and replied with ''{1}''.", new Object[] { this.exifToolArgFile, versionString });
            return false;
        }
        return isVersionString;
    }
