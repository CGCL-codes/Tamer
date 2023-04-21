    private Map<String, byte[]> getEntries(ZipInputStream in) throws IOException {
        Map<String, byte[]> result = new HashMap<String, byte[]>();
        while (true) {
            ZipEntry entry = in.getNextEntry();
            if (entry == null) {
                break;
            }
            if (entry.getName().indexOf("MANIFEST.MF") != -1) {
                continue;
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int c;
            while ((c = in.read(buf)) > 0) bos.write(buf, 0, c);
            result.put(entry.getName(), bos.toByteArray());
        }
        return result;
    }
