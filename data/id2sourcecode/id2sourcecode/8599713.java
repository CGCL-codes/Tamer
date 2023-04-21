    public String transformString(String line) {
        String fields[] = line.split("\\s+");
        if (fields == null || fields.length != 6) {
            String msg = "Invalid Trec Format : " + line;
            throw new IllegalArgumentException(msg);
        }
        fields[2] = url2Docno(MD5.digest(fields[2].trim()));
        StringBuffer sb = new StringBuffer();
        sb.append(fields[0] + "\t");
        sb.append(fields[3] + "\t");
        sb.append(fields[4] + "\t");
        sb.append(fields[2]);
        return sb.toString();
    }
