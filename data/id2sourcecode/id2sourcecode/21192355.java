    public static final String fread(file loc) throws IOException {
        int len = 4096, read;
        byte[] buf = new byte[len];
        InputStream in = loc.getInputStream();
        if (null == in) throw new IOException("File resource (" + loc.path() + ") not found."); else {
            try {
                bbuf bbu = new bbuf();
                while (0 < (read = in.read(buf, 0, len))) bbu.write(buf, 0, read);
                if (0 < bbu.length()) return new String(utf8.decode(bbu.toByteArray())); else return null;
            } finally {
                in.close();
            }
        }
    }
