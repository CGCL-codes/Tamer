    public void file2file(Object file, String startWith, BundleReader reader, BundleWriter writer) throws IOException {
        File source, dest;
        if (file instanceof File) source = dest = (File) file; else {
            File[] arr = (File[]) file;
            source = arr[0];
            dest = arr[1];
        }
        BufferedReader br = new BufferedReader(new FileReader(source));
        try {
            writer.createWriter(getPropertiesPath(dest), encoding);
            boolean header = true;
            Entry entry = new Entry();
            while (br.ready()) {
                String line = br.readLine();
                if (line == null) break;
                if (header) {
                    header = reader.isHeader(line, startWith);
                    continue;
                }
                if (header) continue;
                if (!reader.parseEntry(br, line, entry)) break;
                writer.write(entry);
            }
        } finally {
            br.close();
            writer.close();
        }
    }
