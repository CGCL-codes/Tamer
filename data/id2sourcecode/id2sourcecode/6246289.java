    public void writeSPK(String base, NameFilter filter, File output) throws IOException {
        checkSource(base);
        ArrayList<FileEntry> files = new ArrayList<FileEntry>();
        listFile(base, filter, files);
        int totalsize = 4;
        for (FileEntry file : files) {
            totalsize += 12 + file.getNameutf8().length;
        }
        FileOutputStream out = new FileOutputStream(output);
        for (int i = 0; i < totalsize; i++) {
            out.write(0);
        }
        byte[] buffer = new byte[16384];
        int pos = totalsize;
        int read;
        for (FileEntry file : files) {
            System.out.print("ENCO " + file.name);
            SPKOutputStream sos = new SPKOutputStream(out, seed);
            GZIPOutputStream gos = new GZIPOutputStream(sos);
            InputStream fis = getInputStream(base, file.name);
            while ((read = fis.read(buffer)) >= 0) {
                if (read == 0) {
                    continue;
                }
                gos.write(buffer, 0, read);
            }
            gos.close();
            fis.close();
            file.setData(new FileData(file.getName(), pos, sos.length()));
            pos += sos.length();
            System.out.println(" -> " + sos.length());
        }
        out.close();
        RandomAccessFile raf = new RandomAccessFile(output, "rw");
        raf.writeInt(files.size());
        for (FileEntry file : files) {
            System.out.println("META " + file.name);
            raf.writeInt(8 + file.getNameutf8().length);
            raf.writeInt(file.getData().getPos());
            raf.writeInt(file.getData().getLength());
            raf.write(file.getNameutf8());
        }
        raf.close();
        System.out.println("FINISHED");
    }
