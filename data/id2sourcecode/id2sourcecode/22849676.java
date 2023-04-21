    protected Class loadClassEntry(SearchResult classEntry, String name) throws IOException {
        URL url = classEntry.url;
        DataInputStream is = new DataInputStream(url.openStream());
        byte[] buffer = new byte[classEntry.len];
        is.readFully(buffer);
        return defineClass(name, buffer, 0, buffer.length);
    }
