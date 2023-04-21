    public static synchronized Response handle(Request request) {
        if (cache == null) {
            try {
                cache = new Cache(new File("./data/cache/"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        String path = request.getPath();
        if (path.equals("/")) {
            path = "/index.html";
        }
        String mime = getMimeType(path);
        try {
            if (crcTable == null) {
                crcTable = calculateCrcTable();
            }
            if (path.startsWith("/crc")) {
                return new Response(crcTable.asReadOnlyBuffer(), mime);
            } else if (path.startsWith("/title")) {
                return new Response(cache.getFile(0, 1).getBytes(), mime);
            } else if (path.startsWith("/config")) {
                return new Response(cache.getFile(0, 2).getBytes(), mime);
            } else if (path.startsWith("/interface")) {
                return new Response(cache.getFile(0, 3).getBytes(), mime);
            } else if (path.startsWith("/media")) {
                return new Response(cache.getFile(0, 4).getBytes(), mime);
            } else if (path.startsWith("/versionlist")) {
                return new Response(cache.getFile(0, 5).getBytes(), mime);
            } else if (path.startsWith("/textures")) {
                return new Response(cache.getFile(0, 6).getBytes(), mime);
            } else if (path.startsWith("/wordenc")) {
                return new Response(cache.getFile(0, 7).getBytes(), mime);
            } else if (path.startsWith("/sounds")) {
                return new Response(cache.getFile(0, 8).getBytes(), mime);
            }
            path = new File(FILES_DIRECTORY + path).getAbsolutePath();
            if (!path.startsWith(FILES_DIRECTORY)) {
                return null;
            }
            RandomAccessFile f = new RandomAccessFile(path, "r");
            try {
                MappedByteBuffer data = f.getChannel().map(MapMode.READ_ONLY, 0, f.length());
                return new Response(data, mime);
            } finally {
                f.close();
            }
        } catch (IOException ex) {
            return null;
        }
    }
