    public void write(String name, Scribable object, boolean compress) throws IOException, UnscribableNodeEncountered {
        DirEntry d = (DirEntry) index.get(name);
        if (d != null) {
            Block b = new Block();
            b.length = d.length;
            b.loc = d.pos;
            free.add(b);
        } else {
            d = new DirEntry();
            d.name = name;
            index.put(name, d);
        }
        d.compressed = compress;
        ZipOutputStream zip = null;
        ByteArrayOutputStream bout = new ByteArrayOutputStream(10000);
        OutputStream out = bout;
        if (compress) {
            zip = new ZipOutputStream(out);
            zip.putNextEntry(new ZipEntry("object"));
            out = zip;
        }
        ScribeOutputStream sout = new ScribeOutputStream(out);
        if (object == null) {
            System.out.println("Object is null");
        }
        sout.writeScribable(object);
        if (compress) {
            zip.closeEntry();
        }
        out.close();
        byte[] data = bout.toByteArray();
        d.pos = allocateSpace(data.length);
        d.length = data.length;
        file.seek(d.pos);
        file.write(data, 0, data.length);
        if (debug) {
            Log.log.println(LogType.EXHAUSTIVE, "Wrote out " + d.length + " bytes for " + name);
        }
    }
