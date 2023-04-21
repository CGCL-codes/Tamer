    public BlockDataStorage(File aFile, int fileSize) throws IOException {
        synchronized (this) {
            file = aFile;
            if (file.exists() == false) {
                file.createNewFile();
            }
            FileChannel rwBufferChannel = new RandomAccessFile(file, "rw").getChannel();
            buffer = rwBufferChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            size = fileSize;
            pointer = 0;
            blockSize = size;
            partitionLevel = 0;
            partitionThreshold = 0.375;
            StorageBlock initial = new StorageBlock(this, 0, size);
            blocks = new ArrayList<StorageBlock>();
            blocks.add(initial);
            freeBlocks = new ArrayList<StorageBlock>();
            freeBlocks.add(initial);
        }
    }
