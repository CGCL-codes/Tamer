    @Override
    public String toString() {
        Enumeration e = this.cache.elements();
        StringBuffer buffer = new StringBuffer();
        int i = 1;
        while (e.hasMoreElements()) {
            Entry en = (Entry) e.nextElement();
            buffer.append("[" + i + "] " + en.name + ", " + en.digest() + '\n');
            i++;
        }
        return buffer.toString();
    }
