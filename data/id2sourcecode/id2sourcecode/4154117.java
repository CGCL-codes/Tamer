    public String toTrecFormat(String text) {
        String fields[] = text.split("\t");
        if (fields == null || fields.length != 2) {
            String msg = "Invalid format:" + text;
            throw new IllegalArgumentException(msg);
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<DOC>" + Constants.NEWLINE);
        sb.append("<DOCNO>" + fields[1] + "</DOCNO>" + Constants.NEWLINE);
        sb.append("<URL>" + MD5.digest(fields[0]) + "</URL>" + Constants.NEWLINE);
        sb.append("</DOC>" + Constants.NEWLINE);
        sb.append(Constants.NEWLINE);
        return sb.toString();
    }
