    public Response(InputStream is, OutputStream os, int debugI, String sourceI, boolean headerOnlyI) {
        debug = debugI;
        source = sourceI;
        headerOnly = headerOnlyI;
        boolean done = false;
        int nAvail = 0;
        int nRead = 0;
        DataInputStream br = new DataInputStream(is);
        try {
            while (!done) {
                String nextLine = br.readLine();
                if (debug > 4) System.err.println("Response nextLine " + nextLine);
                if (nextLine == null) done = true; else {
                    if (os != null) {
                        os.write(nextLine.getBytes());
                        os.write(eol.getBytes());
                    }
                    if (nextLine.length() == 0) done = true;
                    headerV.add(nextLine);
                }
            }
            numHeaderLines = headerV.size();
            headerA = new String[numHeaderLines];
            headerV.copyInto(headerA);
            for (int i = numHeaderLines - 1; i >= 0; i--) {
                if (headerA[i].regionMatches(true, 0, contentLengthLabel, 0, contentLengthLabel.length())) {
                    contentLength = Integer.parseInt(headerA[i].substring(contentLengthLabel.length()).trim());
                } else if (headerA[i].regionMatches(true, 0, contentTypeLabel, 0, contentTypeLabel.length())) {
                    if (headerA[i].indexOf(textType) > 0) isText = true;
                } else if (headerA[i].regionMatches(true, 0, lastModifiedLabel, 0, lastModifiedLabel.length())) {
                    try {
                        modifiedDate = new WebDate(headerA[i].substring(lastModifiedLabel.length()).trim());
                    } catch (Exception e) {
                    }
                }
            }
            if (!headerOnly) {
                if (numHeaderLines == 0 || headerV.elementAt(0) == null || ((String) (headerV.elementAt(0))).equals("Error")) {
                    if (debug > 1) System.err.println("Response null header");
                    isNull = true;
                } else if (contentLength == 0 && getStatusCode().equals("200")) {
                    Vector contentV = new Vector();
                    byte[] chunk = new byte[1024];
                    int numRead = 0;
                    while ((numRead = br.read(chunk)) > -1) {
                        contentLength += numRead;
                        if (chunk.length > numRead) {
                            byte[] temp = new byte[numRead];
                            System.arraycopy(chunk, 0, temp, 0, numRead);
                            chunk = temp;
                        }
                        if (os != null) os.write(chunk);
                        contentV.add(chunk);
                        chunk = new byte[1024];
                    }
                    if (os != null) {
                        os.write(eol.getBytes());
                    }
                    content = new byte[contentLength];
                    int j = 0;
                    for (int i = 0; i < contentV.size(); i++) {
                        byte[] nextChunk = (byte[]) contentV.elementAt(i);
                        System.arraycopy(nextChunk, 0, content, j, nextChunk.length);
                        j += nextChunk.length;
                    }
                } else if (contentLength > 0) {
                    content = new byte[contentLength];
                    int numRead = br.read(content, 0, contentLength);
                    if (numRead >= 0) {
                        while (numRead < contentLength) {
                            numRead += br.read(content, numRead, contentLength - numRead);
                        }
                        br.readLine();
                        if (os != null) {
                            os.write(content);
                            os.write(eol.getBytes());
                        }
                    } else {
                        contentLength = 0;
                        headerOnly = true;
                    }
                } else if (isText) {
                    String nextLine = null;
                    while ((nextLine = br.readLine()) != null) {
                        if (os != null) {
                            os.write(nextLine.getBytes());
                            os.write(eol.getBytes());
                        }
                        bodyV.add(nextLine);
                    }
                    if (bodyV.size() > 0) bodyV.remove(bodyV.size() - 1);
                }
            }
            if (os != null) os.close();
        } catch (Exception e) {
            System.err.println("Response exception: " + e.getMessage());
            System.err.println("  os " + os);
            System.err.println("header " + getHeader());
            e.printStackTrace();
            isNull = true;
        }
    }
