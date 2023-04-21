    public synchronized void upload(List<SourceFile> sources) throws IOException {
        setTitle("Uploading");
        calculateTotalSize(sources);
        int count = 0;
        File lock = null;
        File db = new File(uploadDir + Updater.XML_COMPRESSED);
        byte[] buffer = new byte[65536];
        for (SourceFile source : sources) {
            File file = new File(uploadDir + source.getFilename());
            if (lock == null) lock = file;
            File dir = file.getParentFile();
            if (!dir.exists()) dir.mkdirs();
            OutputStream out = new FileOutputStream(file);
            InputStream in = source.getInputStream();
            addItem(source);
            int currentCount = 0;
            int currentTotal = (int) source.getFilesize();
            for (; ; ) {
                int read = in.read(buffer);
                if (read < 0) break;
                out.write(buffer, 0, read);
                currentCount += read;
                setItemCount(currentCount, currentTotal);
                setCount(count + currentCount, total);
            }
            in.close();
            out.close();
            count += currentCount;
            itemDone(source);
        }
        File backup = new File(db.getAbsolutePath() + ".old");
        if (backup.exists()) backup.delete();
        db.renameTo(backup);
        lock.renameTo(db);
        done();
    }
