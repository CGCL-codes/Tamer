    public FileDescriptor(String basePath, File file, int hashSpeedInMbPerSecond, UICallback callback) throws IOException {
        basePath = TextUtils.makeSurePathIsMultiplatform(basePath);
        byte buf[] = new byte[BLOCK_SIZE];
        if (T.t) T.trace("Hashing " + file + " ...");
        long modifiedAtBeforeHashing = file.lastModified();
        Tiger tiger = new Tiger();
        FileInputStream in = new FileInputStream(file);
        ArrayList<Hash> hashes = new ArrayList<Hash>();
        SimpleTimer st = new SimpleTimer();
        long startSize = file.length();
        long totalRead = 0;
        int read;
        long startedLastReadAt = System.currentTimeMillis();
        if (callback != null) callback.statusMessage("Hashing " + file.getName() + "...");
        long updateHashMessageTick = System.currentTimeMillis() + 1500;
        long startTick = System.currentTimeMillis();
        while ((read = in.read(buf)) == buf.length) {
            tiger.update(buf);
            hashes.add(new Hash(tiger.digest()));
            tiger.reset();
            totalRead += read;
            if (hashSpeedInMbPerSecond > 0) {
                long t = System.currentTimeMillis();
                if (t - startedLastReadAt < 1000 / hashSpeedInMbPerSecond) {
                    try {
                        Thread.sleep(1000 / hashSpeedInMbPerSecond - (t - startedLastReadAt));
                    } catch (InterruptedException e) {
                    }
                }
            }
            startedLastReadAt = System.currentTimeMillis();
            if (callback != null && System.currentTimeMillis() - updateHashMessageTick > 500) {
                String s2 = "";
                if (hashSpeedInMbPerSecond > 0) s2 = " [hash speed limit: " + hashSpeedInMbPerSecond + "Mb/s]";
                String s = "Hashing " + file.getName() + " (" + (totalRead * 100 / file.length()) + "% done @ " + TextUtils.formatByteSize(totalRead / ((System.currentTimeMillis() - startTick) / 1000)) + "/s" + s2 + ")";
                callback.statusMessage(s);
                updateHashMessageTick = System.currentTimeMillis();
            }
            if (startSize != file.length()) {
                throw new FileModifiedWhileHashingException("Inconsistent file size! File was probably written to.");
            }
            if (modifiedAtBeforeHashing != file.lastModified()) throw new FileModifiedWhileHashingException("File modified while hashing!");
        }
        if (read != -1) {
            totalRead += read;
            if (totalRead != file.length()) throw new IOException("Inconsistent file size in read!");
            tiger.update(buf, 0, read);
            hashes.add(new Hash(tiger.digest()));
        }
        tiger.reset();
        for (Hash h : hashes) tiger.update(h.array());
        rootHash = new Hash(tiger.digest());
        hashList = hashes.toArray(new Hash[hashes.size()]);
        this.basePath = basePath;
        size = file.length();
        subpath = createSubpath(file.getPath());
        modifiedAt = file.lastModified();
        in.close();
        if (T.t) T.debug("Hashed " + TextUtils.formatNumber("" + totalRead) + " bytes in " + st.getTime() + ". Hash list contains " + hashes.size() + " hashes.");
    }
