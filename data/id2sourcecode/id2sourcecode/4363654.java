    public void run() {
        try {
            ltm.lockConnection();
            ltm.getStream().writeOpCode(OPCodes.GET_IMAGES);
            ltm.getStream().readString();
            ltm.getStream().writeInt(images.size());
            for (String s : images) {
                ltm.getStream().writeString(s);
                if (pl != null) running = pl.toRead(toRead);
                ltm.getStream().writeBoolean(running);
                if (!running) break;
                toRead = ltm.getStream().readInt();
                byte buf[] = new byte[org.magnesia.Constants.CHUNK_SIZE];
                int read = 0;
                if (pl != null) pl.currentRead(0);
                int size = toRead;
                File out = new File(path + "/" + s.substring(s.lastIndexOf("/") + 1));
                FileOutputStream fos = new FileOutputStream(out);
                while (read >= 0 && toRead > 0) {
                    read = ltm.getStream().readData(buf, ((toRead >= buf.length) ? buf.length : toRead));
                    toRead -= read;
                    fos.write(buf, 0, read);
                    transferred = size - toRead;
                    if (pl != null) pl.currentRead(size - toRead);
                }
                fos.flush();
                fos.close();
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ltm.unlockConnection();
            running = false;
            finished = true;
        }
    }
