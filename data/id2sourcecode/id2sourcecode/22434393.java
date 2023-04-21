    private Hashtable processData(ServletInputStream is, String boundary, String saveInDir) throws IllegalArgumentException, IOException {
        if (is == null) throw new IllegalArgumentException("InputStream");
        if (boundary == null || boundary.trim().length() < 1) throw new IllegalArgumentException("boundary");
        boundary = "--" + boundary;
        StringTokenizer stLine = null, stFields = null;
        FileInfo fileInfo = null;
        Hashtable dataTable = new Hashtable(5);
        String line = null, field = null, paramName = null;
        boolean saveFiles = (saveInDir != null && saveInDir.trim().length() > 0), isFile = false;
        if (saveFiles) {
            File f = new File(saveInDir);
            f.mkdirs();
        }
        line = getLine(is);
        if (line == null || !line.startsWith(boundary)) throw new IOException("Boundary not found;" + " boundary = " + boundary + ", line = " + line);
        while (line != null) {
            if (line == null || !line.startsWith(boundary)) return dataTable;
            line = getLine(is);
            if (line == null) return dataTable;
            stLine = new StringTokenizer(line, ";\r\n");
            if (stLine.countTokens() < 2) throw new IllegalArgumentException("Bad data in second line");
            line = stLine.nextToken().toLowerCase();
            if (line.indexOf("form-data") < 0) throw new IllegalArgumentException("Bad data in second line");
            stFields = new StringTokenizer(stLine.nextToken(), "=\"");
            if (stFields.countTokens() < 2) throw new IllegalArgumentException("Bad data in second line");
            fileInfo = new FileInfo();
            stFields.nextToken();
            paramName = stFields.nextToken();
            isFile = false;
            if (stLine.hasMoreTokens()) {
                field = stLine.nextToken();
                stFields = new StringTokenizer(field, "=\"");
                if (stFields.countTokens() > 1) {
                    if (stFields.nextToken().trim().equalsIgnoreCase("filename")) {
                        fileInfo.setName(paramName);
                        String value = stFields.nextToken();
                        if (value != null && value.trim().length() > 0) {
                            fileInfo.setClientFileName(value);
                            isFile = true;
                        } else {
                            line = getLine(is);
                            line = getLine(is);
                            line = getLine(is);
                            line = getLine(is);
                            continue;
                        }
                    }
                } else if (field.toLowerCase().indexOf("filename") >= 0) {
                    line = getLine(is);
                    line = getLine(is);
                    line = getLine(is);
                    line = getLine(is);
                    continue;
                }
            }
            boolean skipBlankLine = true;
            if (isFile) {
                line = getLine(is);
                if (line == null) return dataTable;
                if (line.trim().length() < 1) skipBlankLine = false; else {
                    stLine = new StringTokenizer(line, ": ");
                    if (stLine.countTokens() < 2) throw new IllegalArgumentException("Bad data in third line");
                    stLine.nextToken();
                    fileInfo.setFileContentType(stLine.nextToken());
                }
            }
            if (skipBlankLine) {
                line = getLine(is);
                if (line == null) return dataTable;
            }
            if (!isFile) {
                line = getLineISO(is);
                if (line == null) return dataTable;
                Object prev = dataTable.get(paramName);
                if (prev == null) {
                    dataTable.put(paramName, line);
                } else if (prev instanceof String) {
                    String[] curr = new String[2];
                    curr[0] = (String) prev;
                    curr[1] = line;
                    dataTable.put(paramName, curr);
                } else if (prev instanceof String[]) {
                    String[] aPrev = (String[]) prev;
                    String[] curr = new String[aPrev.length + 1];
                    for (int i = 0; i < aPrev.length; i++) curr[i] = aPrev[i];
                    curr[aPrev.length] = line;
                    dataTable.put(paramName, curr);
                }
                line = getLine(is);
                continue;
            }
            try {
                OutputStream os = null;
                String path = null;
                if (saveFiles) os = new FileOutputStream(path = getFileName(saveInDir, fileInfo.getClientFileName())); else os = new ByteArrayOutputStream(ONE_MB);
                boolean readingContent = true;
                byte previousLine[] = new byte[2 * ONE_MB];
                byte temp[] = null;
                byte currentLine[] = new byte[2 * ONE_MB];
                int read, read3;
                if ((read = is.readLine(previousLine, 0, previousLine.length)) == -1) {
                    line = null;
                    break;
                }
                while (readingContent) {
                    if ((read3 = is.readLine(currentLine, 0, currentLine.length)) == -1) {
                        line = null;
                        break;
                    }
                    if (compareBoundary(boundary, currentLine)) {
                        os.write(previousLine, 0, read);
                        os.flush();
                        line = new String(currentLine, 0, read3);
                        break;
                    } else {
                        os.write(previousLine, 0, read);
                        os.flush();
                        temp = currentLine;
                        currentLine = previousLine;
                        previousLine = temp;
                        read = read3;
                    }
                }
                os.close();
                temp = null;
                previousLine = null;
                currentLine = null;
                if (!saveFiles) {
                    ByteArrayOutputStream baos = (ByteArrayOutputStream) os;
                    fileInfo.setFileContents(baos.toByteArray());
                } else {
                    fileInfo.setLocalFile(new File(path));
                    os = null;
                }
                dataTable.put(paramName, fileInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dataTable;
    }
