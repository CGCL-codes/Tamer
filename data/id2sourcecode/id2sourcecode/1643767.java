    private void writeSourceAudit(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !abort) {
            progress = calculateProgress(line.length());
            String data[] = line.split(SEPARATOR_TAB);
            SourceAudit entry = new SourceAudit();
            entry.setSourceId(data[0]);
            entry.setSourceFullpath(data[1]);
            entry.setSourcePath(data[2]);
            entry.setSourceType(data[3]);
            entry.setSourceMd5(data[4]);
            entry.setSourceParsetime(Integer.valueOf(data[5]));
            entry.setSourceMtime(Integer.valueOf(data[6]));
            insertObject(entry);
        }
        reader.close();
    }
