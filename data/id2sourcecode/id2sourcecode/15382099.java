    @Override
    public List<String> parseStreams() {
        List<String> list = new ArrayList<String>();
        int start = 0;
        boolean isValid = false;
        String input = getContent().toLowerCase();
        while ((start = input.indexOf("<ref", start)) > 0) {
            int offset = input.indexOf("href", start);
            if (offset > 0) {
                start = offset;
                int begin = input.indexOf("=", offset);
                int end = input.indexOf(">", offset);
                String value = getContent().substring(begin + 1, end).trim();
                try {
                    if (value.endsWith("/")) {
                        value = value.substring(0, value.length() - 1);
                    }
                    if (value.endsWith("\"")) {
                        value = value.substring(0, value.length() - 1);
                    }
                    if (value.startsWith("\"")) {
                        value = value.substring(1, value.length());
                    }
                    URI uri = new URI(value);
                    URL url = new URL(value);
                    if (isExisting(list, value)) {
                        ;
                    } else if (uri.getHost() != null) {
                        list.add(value);
                        isValid = true;
                    } else if (url.getProtocol() != null) {
                        list.add(value);
                        isValid = true;
                    }
                } catch (URISyntaxException e1) {
                    Log.writeToStdout(Log.WARNING, "ASXTranslator", "getChannels", "Invalid stream URI " + value);
                } catch (MalformedURLException e) {
                    Log.writeToStdout(Log.WARNING, "ASXTranslator", "getChannels", "Malformed stream URL " + value);
                }
            }
        }
        if (!isValid) {
            list = null;
            Log.writeToStdout(Log.AUDIT, "ASXTranslator", "getChannels", "Invalid playlist");
        }
        return list;
    }
