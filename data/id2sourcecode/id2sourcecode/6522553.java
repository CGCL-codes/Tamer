    private String splitFile(File fileToSplit) {
        BufferedReader bufReader = null;
        this.atLeastOneSplit = false;
        int splitCount = 0;
        try {
            InputStreamReader isReader = new InputStreamReader(new FileInputStream(fileToSplit), FileUtils.UTF_8_ENCODING);
            bufReader = new BufferedReader(isReader, FileUtils.BUFFER_SIZE);
            String tail = "";
            String fileName = "";
            List stringList = new ArrayList();
            List startMarkList = new ArrayList();
            List endMarkList = new ArrayList();
            StringSplitResult result = null;
            String splitOutString = null;
            String startMark = null;
            String endMark = null;
            String headerLine = null;
            int headerLines = 0;
            if (this.copyHeaderLine) {
                ReadFileResults readResults = readLinesFromFile(bufReader, 1);
                headerLine = readResults.output;
                headerLines = 1;
                if (readResults.endOfFile) {
                    return "Could not split up: ";
                }
            }
            while (true) {
                ReadFileResults readResults = null;
                if (this.splitByLines) {
                    readResults = readLinesFromFile(bufReader, outputFileSizeMaxLines - headerLines);
                } else {
                    readResults = readBytesFromFile(bufReader, this.readBuffer);
                }
                String readString = readResults.output;
                if (readString == null) break;
                if (readString.equals("") && tail.equals("")) break;
                if (!readResults.endOfFile || splitCount > 0) {
                    splitCount++;
                }
                if (splitByLines) {
                    fileName = getFileName(fileToSplit, splitCount, readString);
                    writeSplitFile(fileName, readString, headerLine);
                    if (readResults.endOfFile) {
                        break;
                    }
                } else {
                    String stringToSplit = tail + readString;
                    result = stringSplitter.splitString(startPattern, endPattern, this.outputStartAndEndMarks, stringToSplit);
                    stringList = result.getSplitList();
                    startMarkList = result.getStartMarkList();
                    endMarkList = result.getEndMarkList();
                    for (int i = 0; i < stringList.size(); i++) {
                        splitOutString = (String) stringList.get(i);
                        if (this.outputStartAndEndMarks) {
                            fileName = getFileName(fileToSplit, splitCount, splitOutString);
                        } else {
                            startMark = (String) startMarkList.get(i);
                            endMark = (String) endMarkList.get(i);
                            fileName = getFileName(fileToSplit, splitCount, startMark + splitOutString + endMark);
                        }
                        writeSplitFile(fileName, splitOutString, headerLine);
                    }
                    tail = getTail(fileName, result);
                    if ((endPattern.equals("") || this.usingDelimiter) && (stringList.size() == 0) && (readResults.endOfFile == true)) {
                        writeSplitFile(fileName, tail, headerLine);
                        break;
                    }
                    if ((stringList.size() == 0) && readString.equals("")) break;
                }
            }
        } catch (IOException e) {
            errEntry.setThrowable(e);
            errEntry.setAppContext("splitFile()");
            errEntry.setDocInfo(fileToSplit.toString());
            errEntry.setAppMessage("Error reading file.");
            logger.logError(errEntry);
            return null;
        } finally {
            try {
                if (bufReader != null) bufReader.close();
            } catch (IOException e) {
                errEntry.setThrowable(e);
                errEntry.setAppContext("splitFile()");
                errEntry.setDocInfo(fileToSplit.toString());
                errEntry.setAppMessage("Error reading file.");
                logger.logError(errEntry);
                return null;
            }
        }
        if (atLeastOneSplit) {
            return "Split up: ";
        }
        return "Could not split up: ";
    }
