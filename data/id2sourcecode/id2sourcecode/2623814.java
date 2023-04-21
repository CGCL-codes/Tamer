    @SuppressWarnings("deprecation")
    private ArrayList setEntries(String messageFilePath) {
        ArrayList entries = new ArrayList();
        Parser parser;
        if (encoding == "XML") {
            parser = new DefaultXMLParser();
        } else if (encoding == "VB") {
            parser = new PipeParser();
        } else {
            parser = new GenericParser();
        }
        try {
            URL url = MessageLibrary.class.getClassLoader().getResource(messageFilePath);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer msgBuf = new StringBuffer();
            HashMap segments = new HashMap();
            Message msg = null;
            boolean eof = false;
            boolean inComment = false;
            while (!eof) {
                String line = in.readLine();
                System.out.println("Line Read Result************" + line);
                if (line == null) {
                    eof = true;
                } else {
                    if (line.startsWith(SINGLE_LINE_COMMENT)) {
                        continue;
                    } else if (inComment) {
                        if (line.substring(0, 3).trim().startsWith(MULTI_LINE_COMMENT_END)) {
                            inComment = false;
                        }
                        continue;
                    } else if (line.startsWith(MULTI_LINE_COMMENT_START)) {
                        inComment = true;
                        continue;
                    }
                }
                if (!eof && line.length() > 0) {
                    String[] lineSplit = line.split("\\|");
                    String lineKey = lineSplit[0];
                    try {
                        Integer.parseInt(lineSplit[1]);
                        lineKey = lineKey + "|" + lineSplit[1];
                    } catch (NumberFormatException e) {
                        int stop = 1;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        int stop = 1;
                    }
                    segments.put(lineKey, line);
                    msgBuf.append(line + "\r");
                } else if (msgBuf.length() > 0) {
                    String msgStr = msgBuf.toString();
                    try {
                        msg = parser.parse(msgStr);
                        Status.writeStatus("Message:\n" + msgStr);
                    } catch (Exception e) {
                        ++numParsingErrors;
                        Status.writeStatus("Warning: Parsing errors with message:\n" + msgStr);
                        Log.tryToLog(e, "Parsing errors with message:\n" + msgStr);
                    }
                    entries.add(new LibraryEntry(new String(msgStr), new HashMap(segments), msg));
                    msgBuf.setLength(0);
                    segments.clear();
                    msg = null;
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            Status.writeStatus("Warning: Message file not found: " + messageFilePath);
            Log.tryToLog(e, "Message file not found " + messageFilePath);
        } catch (IOException e) {
            Status.writeStatus("Warning: Unable to open message file: " + messageFilePath);
            Log.tryToLog(e, "Unable to open message file: " + messageFilePath);
        }
        return entries;
    }
