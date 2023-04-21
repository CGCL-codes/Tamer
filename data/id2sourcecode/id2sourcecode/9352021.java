    public HelpIndexList parseHelp() throws HelpParserException {
        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(url.openStream()));
            String tempLine;
            list = new HelpIndexList();
            int line = 1;
            tempLine = read.readLine();
            while (tempLine != null) {
                tempLine = tempLine.trim();
                if (tempLine.indexOf("<topic") == 0) {
                    int topic = tempLine.indexOf(" topic");
                    int location = tempLine.indexOf("location");
                    if (topic < 0) throw new HelpParserException("Missing topic " + "attribute on line: " + line);
                    if (location < 0) throw new HelpParserException("Missing location " + "attribute on line: " + line);
                    list.addHelpNode(new HelpIndexNode(tempLine.substring(tempLine.indexOf('"', topic) + 1, tempLine.indexOf('"', topic + 8)), tempLine.substring(tempLine.indexOf('"', location) + 1, tempLine.indexOf('"', location + 10))));
                }
                tempLine = read.readLine();
                line++;
            }
            read.close();
        } catch (IOException e) {
            throw new HelpParserException("Error with file " + e);
        }
        return list;
    }
