    public LingPipeNER(boolean caseSensitive, boolean allMatches) {
        try {
            long startTime = System.currentTimeMillis();
            logger.info("Initializing LingPipe NER...");
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(DICTIONARY_PATH);
            File f = File.createTempFile("dbpedia_lingpipe", ".dictionary");
            f.deleteOnExit();
            OutputStream out = new FileOutputStream(f);
            byte buf[] = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) out.write(buf, 0, len);
            out.close();
            is.close();
            Dictionary<String> dictionary = (Dictionary<String>) AbstractExternalizable.readObject(f);
            ner = new ExactDictionaryChunker(dictionary, IndoEuropeanTokenizerFactory.INSTANCE, allMatches, caseSensitive);
            logger.info("Done in " + (System.currentTimeMillis() - startTime) + "ms.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
