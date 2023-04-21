    public void run() {
        if (this.bolStreamFormat) {
            try {
                this.insLoadMe = this.urlLoadMe.openStream();
                this.bolStreamLoaded = true;
            } catch (IOException ioe) {
                this.bolStreamLoaded = false;
            }
        }
    }
