    public static void main(String[] args) throws MongoException, IOException {
        Mongo m = new Mongo("localhost", 27017);
        DB db = m.getDB("biocloud");
        FastaParser parser = new FastaParser();
        String filename = "6400seqs";
        List<File> listFiles = parser.parse("fasta/" + filename, new File("/tmp"));
        int slice = 1;
        for (File file : listFiles) {
            BasicDBObject doc = new BasicDBObject();
            doc.put("filename", filename);
            doc.put("contigName", file.getName());
            doc.put("timestamp", System.currentTimeMillis());
            doc.put("format", "FASTA");
            doc.put("contentEncoding", "UTF-8");
            doc.put("sliceNumber", slice);
            InputStream is = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[64 * 1024];
            int read = 0;
            while ((read = is.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, read);
            }
            baos.flush();
            doc.put("content", new String(baos.toByteArray(), "UTF-8"));
            if (db.collectionExists("biocloud")) {
                db.getCollection("biocloud").insert(doc);
            } else {
                db.createCollection("biocloud", doc);
                BasicDBObject indexes = new BasicDBObject();
                indexes.append("timestamp", 1).append("filename", 1).append("contigName", 1).append("sliceNumber", 1);
                db.getCollection("biocloud").createIndex(indexes);
            }
            slice++;
        }
    }
