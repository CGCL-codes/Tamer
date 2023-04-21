    public void doUpload(JSONObject event, String fileName, String contentType, java.io.InputStream input, long size) throws java.io.IOException {
        theProgress = 0;
        theScale = 5;
        isCanceled = false;
        isFinished = false;
        theText = "Uploading file " + fileName;
        prisms.ui.UI ui = theSession.getUI();
        ui.startTimedTask(new prisms.ui.UI.ProgressInformer() {

            public void cancel() throws IllegalStateException {
                isCanceled = true;
            }

            public int getTaskProgress() {
                return theProgress;
            }

            public int getTaskScale() {
                return theScale;
            }

            public String getTaskText() {
                System.out.println("Getting text: " + theText);
                return theText;
            }

            public boolean isCancelable() {
                return true;
            }

            public boolean isTaskDone() {
                return isCanceled || isFinished;
            }
        });
        theProgress++;
        try {
            if (contentType.startsWith("text")) {
                java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
                int read = input.read();
                while (read >= 0) {
                    bos.write(read);
                    read = input.read();
                }
                String content = new String(bos.toByteArray());
                log.info("Uploaded text file " + fileName + " of type " + contentType + " of size " + size + ":\n" + content);
            } else {
                log.info("Uploaded binary file " + fileName + " of type " + contentType + " of size " + size);
            }
        } finally {
            input.close();
        }
        while (theProgress < theScale) {
            if (isCanceled) {
                theProgress = theScale;
                continue;
            }
            theProgress++;
            theText = "Counting to " + theScale + ": " + theProgress;
            System.out.println("Changing text to " + theText);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        isFinished = true;
    }
