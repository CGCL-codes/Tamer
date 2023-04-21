        FileStruct(long name, boolean isCurrent) throws IOException {
            this.name = name;
            if (isCurrent) {
                file = new RandomAccessFile(dir + name, "rw");
                file.setLength(fileMaxSize);
            } else file = new RandomAccessFile(dir + name, "r");
            FileChannel fc = file.getChannel();
            long fileSize = fc.size();
            MappedByteBuffer buffer = null;
            if (isCurrent) {
                buffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
                buffer.put((byte) 0);
            } else buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            buf = buffer;
            keysFile = new File(keysdir + name, "");
            if (isCurrent) {
                if (!keysFile.exists()) keysFile.createNewFile();
                keys = new FileOutputStream(keysFile);
            }
            logger.trace("mapped file " + dir + name + " size=" + fileSize);
            assert buf != null;
        }
