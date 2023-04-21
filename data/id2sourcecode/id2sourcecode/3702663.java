    public RawFileNumberProvider() throws FileNotFoundException, Exception {
        if (MAX_MAPPED_BUFFER_SIZE % 4 != 0) throw new InternalError("Veľkosť buffra musí byť deliteľná 4");
        File f = new File(RAW_FILE_PATH);
        if ((fileSize = f.length()) < 5) throw new Exception(String.format("Raw súbor '%s' musí mať aspoň 5 bajtov", RAW_FILE_PATH));
        fch = new FileInputStream(f.getAbsolutePath()).getChannel();
        mapNextBytes();
    }
