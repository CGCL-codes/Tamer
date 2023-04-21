    @Override
    public List<String> parseStreams() {
        List<String> list = new ArrayList<String>();
        boolean isValid = false;
        String[] lines = StringHelper.parseDelimited(this.getContent(), "\r\n");
        for (int i = 0; (lines != null && i < lines.length); i++) {
            String line = lines[i];
            line = line.trim();
            if (line.startsWith("#EXTINF")) {
                isValid = true;
            } else if (line.startsWith("#EXTM3U")) {
                isValid = true;
            } else if (line.startsWith("#")) {
                isValid = true;
            } else {
                try {
                    URI uri = new URI(line);
                    URL url = new URL(line);
                    if (isExisting(list, line)) {
                        ;
                    } else if (uri.getHost() != null) {
                        list.add(line);
                        isValid = true;
                    } else if (url.getProtocol() != null) {
                        list.add(line);
                        isValid = true;
                    }
                } catch (URISyntaxException e1) {
                    Log.writeToStdout(Log.WARNING, "M3UParser", "getChannels", "Invalid M3U stream URI " + line);
                } catch (MalformedURLException e) {
                    Log.writeToStdout(Log.WARNING, "M3UParser", "getChannels", "Malformed M3U stream URL " + line);
                }
            }
        }
        if (!isValid) {
            list = null;
            Log.writeToStdout(Log.AUDIT, "M3UParser", "getStreamsAsList", "Invalid M3U play list");
        }
        return list;
    }
