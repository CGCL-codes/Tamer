    public FileOutputStream(java.io.File file, boolean append) throws FileNotFoundException {
        super(new FileDescriptor());
        this.file = (x.java.io.File) file;
        this.append = append;
        this.file.checkWritable();
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                throw new FileNotFoundException(e.getMessage());
            }
        }
        try {
            URL url = this.file.toURL();
            String oldContent = null;
            if (append) {
                oldContent = (String) url.getContent();
            }
            this.os = url.openConnection().getOutputStream();
            if (append && oldContent != null && oldContent.length() > 0) {
                this.os.write(oldContent.getBytes());
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException(this.file.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
