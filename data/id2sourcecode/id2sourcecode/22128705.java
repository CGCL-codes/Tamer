    public boolean addBinary(String key, URL url) {
        if (_binaries.contains(key)) return false;
        byte chunk[], data[];
        Vector chunks = new Vector();
        int size, cntr, place;
        try {
            InputStream is = url.openStream();
            do {
                chunk = new byte[10000];
                size = is.read(chunk, 0, 10000);
                if (size == 10000) chunks.addElement(chunk);
            } while (size == 10000);
            is.close();
        } catch (IOException ioe) {
            System.err.println("ResMan could not get binary for " + key);
            return false;
        }
        if (size == -1) data = new byte[chunks.size() * 10000]; else data = new byte[chunks.size() * 10000 + size];
        place = 0;
        for (cntr = 0; cntr < chunks.size(); ++cntr) {
            System.arraycopy(chunks.elementAt(cntr), 0, data, place, 10000);
            place += 10000;
        }
        if (size > 0) System.arraycopy(chunk, 0, data, place, size);
        _binaries.put(key, data);
        return true;
    }
