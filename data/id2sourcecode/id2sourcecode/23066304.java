    public LogImpl() {
        try {
            File logFile = new File(LOG_FILENAME);
            if (!logFile.exists() || logFile.length() == 0) {
                this.escritor = new FileOutputStream(logFile);
                this.escritor.write('L');
                this.escritor.write('O');
                this.escritor.write('G');
                this.escritor.write(':');
                this.escritor.flush();
            } else {
                this.escritor = new FileOutputStream(logFile, true);
                this.escritor.getChannel().position(logFile.length());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
