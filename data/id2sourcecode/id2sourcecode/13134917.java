    public FileOggInput(FileInputStream fin) throws FileNotFoundException {
        this.fin = fin;
        this.channel = fin.getChannel();
    }
