    private String loadQuery(String sparql) throws IOException {
        File file = new File(sparql);
        if (file.exists()) {
            FileInputStream stream = null;
            try {
                stream = new FileInputStream(file);
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                return Charset.defaultCharset().decode(bb).toString();
            } finally {
                if (stream != null) stream.close();
            }
        } else {
            return sparql;
        }
    }
