    public void writeln(String text) {
        synchronized (this.treeLock) {
            if (this.reader != null) {
                try {
                    this.reader.write(text + "\r\n");
                } catch (IOException ioe) {
                }
            }
        }
    }
