    public byte[] getImage(String image, int width, ProgressListener pl) {
        check();
        try {
            lock.lock();
            swrap.writeOpCode(OPCodes.GET_IMAGE);
            swrap.writeString(image);
            swrap.writeInt(width);
            int toRead = swrap.readInt();
            if (pl != null) pl.toRead(toRead);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte buf[] = new byte[org.magnesia.Constants.CHUNK_SIZE];
            int read = 0;
            if (pl != null) pl.currentRead(0);
            int size = toRead;
            while (read >= 0 && toRead > 0) {
                read = swrap.readData(buf, ((toRead >= buf.length) ? buf.length : toRead));
                toRead -= read;
                bos.write(buf, 0, read);
                if (pl != null) pl.currentRead(size - toRead);
            }
            lock.unlock();
            return bos.toByteArray();
        } catch (WrapperException e) {
            e.printStackTrace();
        }
        return null;
    }
