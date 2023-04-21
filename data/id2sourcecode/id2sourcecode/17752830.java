        MemoryMappedFileBuffer(File file) {
            try {
                FileInputStream fileStream = new FileInputStream(file);
                FileChannel fileChannel = fileStream.getChannel();
                mFileBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, fileChannel.size());
                mFileBuffer.order(ByteOrder.LITTLE_ENDIAN);
                fileChannel.close();
                fileStream.close();
            } catch (IOException exc) {
                throw new RuntimeIOException(exc.getMessage(), exc);
            }
        }
