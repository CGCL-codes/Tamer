    public Playlist(MediaFile playlistFile) throws PlaylistParseException {
        if (songExtensions == null) {
            songExtensions = new HashSet<String>();
            songExtensions.add("wav");
            songExtensions.add("mp2");
            songExtensions.add("mp3");
            songExtensions.add("aac");
            songExtensions.add("ogg");
            songExtensions.add("wma");
        }
        String fileContent = null;
        try {
            FileReader fr = playlistFile.getFileReader();
            StringWriter sw = new StringWriter();
            char[] data = new char[2024];
            int read = 0;
            while ((read = fr.read(data)) > 0) {
                sw.write(data, 0, read);
            }
            fileContent = sw.getBuffer().toString();
        } catch (IOException e) {
            throw new PlaylistParseException("Exception while loading playlist file.", e);
        }
        String extensionRegEx = "";
        for (Iterator ext = songExtensions.iterator(); ext.hasNext(); ) {
            extensionRegEx += ext.next();
            if (ext.hasNext()) {
                extensionRegEx += " | ";
            }
        }
        extensionRegEx = "(" + extensionRegEx + ")";
        Set<PlaylistEntry> result = new HashSet<PlaylistEntry>();
        if (fileContent.startsWith("#EXTM3U")) {
            fileContent = fileContent.substring("#EXTM3U".length());
            playlistType = TYPE_M3U;
            String lineBreak = "(\\r\\n)";
            String patternLine1 = "EXTINF.*";
            String patternLine2 = ".*";
            String patternEntry = patternLine1 + lineBreak + patternLine2;
            Pattern regexEntry = Pattern.compile(lineBreak + "?(" + patternEntry + ")+");
            Matcher matchEntry = regexEntry.matcher(fileContent);
            String unparsedEntry = null;
            while (matchEntry.find()) {
                unparsedEntry = matchEntry.group();
                try {
                    int length = Integer.parseInt(unparsedEntry.substring("#EXTINF:".length() - 1, unparsedEntry.indexOf(',')));
                    String title = unparsedEntry.substring(unparsedEntry.indexOf(',') + 1, unparsedEntry.indexOf("\r\n")).replace('\r', ' ').replace('\n', ' ').trim();
                    File file = new File(unparsedEntry.substring(unparsedEntry.indexOf("\r\n") + 2));
                    result.add(new PlaylistEntry(title, file, length));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } else if (fileContent.startsWith("[playlist]")) {
            playlistType = TYPE_PLS;
        } else {
            throw new PlaylistParseException("Unknown playlist format.");
        }
        this.playlistEntries = result;
    }
