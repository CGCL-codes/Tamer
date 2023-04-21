    public Hashtable processData(ServletInputStream is, String boundary, String saveInDir) throws IllegalArgumentException, IOException {
        if (is == null) throw new IllegalArgumentException("InputStream");
        if (boundary == null || boundary.trim().length() < 1) throw new IllegalArgumentException("\"" + boundary + "\" is an illegal boundary indicator");
        boundary = "--" + boundary;
        StringTokenizer stLine = null, stFields = null;
        UploadFileInfo fileInfo = null;
        Hashtable dataTable = new Hashtable(5);
        String line = null, field = null, paramName = null;
        boolean saveFiles = (saveInDir != null && saveInDir.trim().length() > 0);
        boolean isFile = false;
        if (saveFiles) {
            File f = new File(saveInDir);
            f.mkdirs();
        }
        line = getLine(is);
        if (line == null || !line.startsWith(boundary)) throw new IOException("Boundary not found; boundary = " + boundary + ", line = " + line);
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
            fileInfo = new UploadFileInfo();
            stFields.nextToken();
            paramName = stFields.nextToken();
            isFile = false;
            if (stLine.hasMoreTokens()) {
                field = stLine.nextToken();
                stFields = new StringTokenizer(field, "=\"");
                if (stFields.countTokens() > 1) {
                    if (stFields.nextToken().trim().equalsIgnoreCase("filename")) {
                        fileInfo.name = paramName;
                        String value = stFields.nextToken();
                        if (value != null && value.trim().length() > 0) {
                            fileInfo.clientFileName = value;
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
                    fileInfo.fileContentType = stLine.nextToken();
                }
            }
            if (skipBlankLine) {
                line = getLine(is);
                if (line == null) return dataTable;
            }
            if (!isFile) {
                line = getLine(is);
                if (line == null) return dataTable;
                dataTable.put(paramName, line);
                line = getLine(is);
                continue;
            }
            try {
                OutputStream os = null;
                String path = null;
                if (saveFiles) os = new FileOutputStream(path = getFileName(saveInDir, fileInfo.clientFileName)); else os = new ByteArrayOutputStream(ONE_MB);
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
                        os.write(previousLine, 0, read - 2);
                        line = new String(currentLine, 0, read3);
                        break;
                    } else {
                        os.write(previousLine, 0, read);
                        temp = currentLine;
                        currentLine = previousLine;
                        previousLine = temp;
                        read = read3;
                    }
                }
                os.flush();
                os.close();
                if (!saveFiles) {
                    ByteArrayOutputStream baos = (ByteArrayOutputStream) os;
                    fileInfo.setFileContents(baos.toByteArray());
                } else fileInfo.file = new File(path);
                dataTable.put(paramName, fileInfo);
            } catch (IOException e) {
                throw e;
            }
        }
        return dataTable;
    }
