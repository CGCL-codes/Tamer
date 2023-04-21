    public void init(String name, boolean read, boolean write, boolean append) throws FileException, FileNotFoundException {
        this.m_File = new java.io.File(name);
        boolean fileExists = this.m_File.exists();
        if (!fileExists && read && !write) throw new FileNotFoundException("File doesn't exist!");
        if (!fileExists) this.m_File = createFile(name); else if (!append) {
            this.m_File.delete();
            this.m_File = createFile(name);
        }
        if (write) this.m_Writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.m_File)));
    }
