    private void defragmentTo(File dest) throws IOException {
        SimpleTimer st = new SimpleTimer();
        File file = new File(dest + ".incomplete");
        FileOutputStream out = new FileOutputStream(file);
        byte buf[] = new byte[BLOCK_SIZE];
        Hash hl[] = new Hash[getNumberOfBlocks()];
        Tiger tiger = new Tiger();
        for (int i = 0; i < getNumberOfBlocks(); i++) {
            if (T.t) T.ass(isOpen(), "File seems to be closed!");
            raf.seek(getOffset(i));
            int read = raf.read(buf, 0, getBlockSize(i));
            if (T.t) T.ass(read == getBlockSize(i), "could not read entire block");
            tiger.reset();
            tiger.update(buf, 0, read);
            hl[i] = new Hash(tiger.digest());
            out.write(buf, 0, read);
        }
        tiger.reset();
        for (Hash h : hl) tiger.update(h.array());
        Hash root = new Hash(tiger.digest());
        if (!root.equals(fd.getRootHash())) {
            file.delete();
            throw new IOException("Integrity check failed when defragmeting file!");
        }
        out.flush();
        out.close();
        if (!file.renameTo(dest)) throw new IOException("Could not rename from " + file + " to " + dest);
        if (T.t) T.info("Defragmented and verified integrity in " + st.getTime() + ".");
    }
