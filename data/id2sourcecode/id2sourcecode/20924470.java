    private void write(String txt) {
        InputStream is = new StringInputStream(txt);
        try {
            int item;
            while ((item = is.read()) != -1) this.out.write(item);
            this.out.println();
        } catch (IOException e) {
            throw new BuildException("Error while writing StopWatch");
        }
    }
