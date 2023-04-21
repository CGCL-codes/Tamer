    public Object remove(int pos) {
        try {
            IntBuffer index = getIndex();
            if (index != null) {
                int filePosition = index.get(pos * 2);
                int objectSize = index.get(pos * 2 + 1);
                FileChannel roChannel = new RandomAccessFile(new File(filename), "rw").getChannel();
                ByteBuffer buf = roChannel.map(FileChannel.MapMode.PRIVATE, filePosition, objectSize);
                buf.clear();
                byte[] byteBuf = new byte[objectSize];
                buf.get(byteBuf, 0, byteBuf.length);
                ObjectInputStream objStream = new ObjectInputStream(new ByteArrayInputStream(byteBuf));
                Object object = objStream.readObject();
                buf = null;
                ByteBuffer before = roChannel.map(FileChannel.MapMode.PRIVATE, 0, filePosition - 1);
                ByteBuffer after = null;
                if ((filePosition + objectSize) < (int) roChannel.size()) after = roChannel.map(FileChannel.MapMode.PRIVATE, filePosition + objectSize, (int) roChannel.size() - (filePosition + objectSize));
                roChannel.close();
                ByteBuffer beforeCopy = ByteBuffer.allocateDirect(before.capacity());
                beforeCopy.put(before);
                beforeCopy.flip();
                before.clear();
                before = null;
                ByteBuffer afterCopy = null;
                if (after != null) {
                    afterCopy = ByteBuffer.allocateDirect(after.capacity());
                    afterCopy.put(after);
                    afterCopy.flip();
                    after.clear();
                }
                after = null;
                FileChannel rwChannel = new RandomAccessFile(new File(filename), "rw").getChannel();
                int numWritten = rwChannel.write(beforeCopy);
                if (afterCopy != null) numWritten = rwChannel.write(afterCopy);
                rwChannel.force(false);
                rwChannel.close();
                before = null;
                after = null;
                roChannel = new RandomAccessFile(new File(indexname), "rw").getChannel();
                before = roChannel.map(FileChannel.MapMode.PRIVATE, 0, pos * 2 - 1);
                if ((pos * 2 + 4 * 2) < (int) roChannel.size()) after = roChannel.map(FileChannel.MapMode.PRIVATE, pos * 2 + 4 * 2, (int) roChannel.size() - (pos * 2 + 4 * 2));
                roChannel.close();
                buf = null;
                beforeCopy = ByteBuffer.allocateDirect(before.capacity());
                beforeCopy.put(before);
                beforeCopy.flip();
                before.clear();
                before = null;
                afterCopy = null;
                if (after != null) {
                    afterCopy = ByteBuffer.allocateDirect(after.capacity());
                    afterCopy.put(after);
                    afterCopy.flip();
                    after.clear();
                }
                after = null;
                rwChannel = new RandomAccessFile(new File(indexname), "rw").getChannel();
                numWritten = rwChannel.write(beforeCopy);
                if (afterCopy != null) numWritten = rwChannel.write(afterCopy);
                rwChannel.force(false);
                rwChannel.close();
                before = null;
                after = null;
                mIndex = null;
                return object;
            }
        } catch (Exception ex) {
            Tools.logException(FileList.class, ex);
            ex.printStackTrace();
        }
        return null;
    }
