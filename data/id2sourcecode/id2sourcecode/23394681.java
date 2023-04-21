    public void run() {
        int readAmount = 0;
        byte[] buffer = new byte[bufferSize];
        try {
            while (readAmount != -1 && !isStoped) {
                try {
                    while ((readAmount = read.read(buffer)) != -1) {
                        write.write(buffer, 0, readAmount);
                        if (readAmount > 0) {
                            System.out.println("DEBUG: from " + readId + " readed " + readAmount + " bytes and send to " + writeId + ";");
                            readAmount = 0;
                        }
                        if (isStoped) {
                            break;
                        }
                    }
                } catch (InterruptedIOException e) {
                }
            }
        } catch (Exception e) {
            System.err.println("An error for " + readId + ":" + writeId + " :" + e);
        } finally {
            System.out.println("All done for " + readId + ":" + writeId + " reader. Internal state is: " + isStoped + ":" + readAmount + " Execute afterend scenario.");
            afterExecute.run();
        }
    }
