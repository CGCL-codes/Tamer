    private void copyDiagrams(File file, String encoding, PrintWriter writer) throws IOException {
        ZipInputStream zis = new ZipInputStream(toURL(file).openStream());
        SubInputStream sub = new SubInputStream(zis);
        ZipEntry currentEntry = null;
        while ((currentEntry = sub.getNextEntry()) != null) {
            if (currentEntry.getName().endsWith(".pgml")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(sub, encoding));
                String firstLine = reader.readLine();
                if (firstLine.startsWith("<?xml")) {
                    reader.readLine();
                } else {
                    writer.println(firstLine);
                }
                readerToWriter(reader, writer);
                sub.close();
                reader.close();
            }
        }
        zis.close();
    }
