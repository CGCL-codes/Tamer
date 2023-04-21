    public Wad(File file, boolean write) throws FileNotFoundException, UnableToBackUpWADFileException, UnableToReadWADFileException {
        wadfilelocation = file;
        if (write) {
            wadfile = new RandomAccessFile(file, "rw");
        } else {
            wadfile = new RandomAccessFile(file, "r");
        }
        wadfilechannel = wadfile.getChannel();
        File temp = new File(wadfilelocation.getAbsolutePath() + ".bak");
        int numericextension = 2;
        while (temp.exists()) {
            temp = new File(wadfilelocation.getAbsolutePath() + ".bak" + "." + numericextension);
            numericextension++;
        }
        RandomAccessFile tempraf = new RandomAccessFile(temp, "rw");
        try {
            tempraf.getChannel().transferFrom(wadfilechannel, 0, wadfilechannel.size());
        } catch (IOException e) {
            throw new UnableToBackUpWADFileException("WAD file could not be backed up.", e);
        }
        WadByteBuffer header = new WadByteBuffer(wadfilechannel, 12, 0);
        identifier = header.getInt(0);
        WadByteBuffer directory = new WadByteBuffer(wadfilechannel, header.getInt(4) * 16, header.getInt(8));
        lumps = new LinkedList<Lump>();
        for (int i = 0; i < header.getInt(4); i++) {
            int pointer = directory.getInt();
            int size = directory.getInt();
            String name = directory.getEightByteString();
            try {
                lumps.add(new Lump(name, size, wadfilechannel, pointer));
            } catch (UnableToInitializeLumpException e) {
                throw new UnableToReadWADFileException("A lump in the WAD file could not be intialized.", e);
            }
        }
    }
