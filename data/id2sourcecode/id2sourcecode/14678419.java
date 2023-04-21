    void readFileHeader(SchemaDefinition sd, ConfigLocation sl) throws IOException {
        if (sd.getGeneratedFileHeader() != null) {
            StringBuffer sb = new StringBuffer();
            if (sl.getJarFilename() != null) {
                URL url = new URL("jar:file:" + sl.getJarFilename() + "!/" + sl.getJarDirectory() + "/" + sd.getGeneratedFileHeader());
                LineNumberReader in = new LineNumberReader(new InputStreamReader(url.openStream()));
                String str;
                while ((str = in.readLine()) != null) {
                    sb.append(str + "\n");
                }
                in.close();
            } else {
                LineNumberReader in = new LineNumberReader(new FileReader(sl.getDirectory() + File.separator + sd.getGeneratedFileHeader()));
                String str;
                while ((str = in.readLine()) != null) {
                    sb.append(str + "\n");
                }
                in.close();
            }
            fileHeader = sb.toString();
        }
    }
