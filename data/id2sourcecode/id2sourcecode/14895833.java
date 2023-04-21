    public void run() {
        if (fileList == null) {
            this.fileList = new Vector();
            this.fileList.add(this.url);
            try {
                this.url.openStream().close();
                while (shouldContinue()) {
                    InputStream page = this.url.openStream();
                    while (page.available() > 0) {
                        int i = fool(page.read());
                    }
                    page.close();
                    this.counter++;
                    this.setChanged();
                    this.notifyObservers("count");
                    this.thread.sleep(this.pauseTime.intValue());
                }
            } catch (java.io.IOException ioe) {
                System.err.println("URL does not exist, we may have won.");
            } catch (java.lang.InterruptedException ie) {
                System.out.println("Thread did not sleep well.");
            }
        }
        System.out.println("dead");
    }
