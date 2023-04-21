    public FileWriter(java.io.File file, boolean append) throws IOException {
        super(new FileDescriptor());
        this.file = (x.java.io.File) file;
        this.file.checkWritable();
        if (!this.file.exists()) {
            this.file.createNewFile();
        }
        try {
            URL url = file.toURL();
            String oldContent = null;
            if (append) {
                oldContent = (String) url.getContent();
            }
            this.osw = new OutputStreamWriter(url.openConnection().getOutputStream());
            if (append && oldContent != null && oldContent.length() > 0) {
                this.osw.write(oldContent);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new FileNotFoundException(file.toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
