    public void deleteEmptyLines(Reader reader, Writer writer) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line = null;
        boolean inPreTag = false;
        while ((line = in.readLine()) != null) {
            String lineRest = line;
            int indexPreTagOpen = -1;
            if (inPreTag && line.indexOf("</pre") != -1) {
                inPreTag = false;
                lineRest = lineRest.substring(lineRest.indexOf(">", line.indexOf("</pre")));
            }
            while ((indexPreTagOpen = lineRest.indexOf("<pre")) != -1) {
                inPreTag = true;
                int indexPreTagClosing = lineRest.indexOf(">", indexPreTagOpen);
                lineRest = lineRest.substring(indexPreTagClosing + 1);
                int indexEndTagPre = lineRest.indexOf("</pre");
                if (indexEndTagPre != -1) {
                    inPreTag = false;
                    lineRest = lineRest.substring(lineRest.indexOf(">", indexEndTagPre));
                }
            }
            if (inPreTag || !line.trim().equals("")) {
                writer.write(line + "\n");
            }
        }
    }
