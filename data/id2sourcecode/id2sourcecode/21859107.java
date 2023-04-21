    public String getDefinition(String text) {
        String def = "";
        try {
            URL url = new URL("http://wordnet.princeton.edu/perl/webwn?s=" + sanitize(text));
            URLConnection connection = url.openConnection();
            DataInputStream in = new DataInputStream(connection.getInputStream());
            String line;
            String newLine;
            int cutTextOffIndex;
            Pattern defPattern = Pattern.compile("<li>(.+)</li>");
            while ((line = in.readLine()) != null) {
                Matcher match = defPattern.matcher(line);
                if (match.find()) {
                    newLine = match.group(1).replaceAll("\\<.*?>", "");
                    cutTextOffIndex = newLine.indexOf(")") + 1;
                    def += "\n" + newLine.substring(cutTextOffIndex) + ".";
                }
            }
            in.close();
        } catch (Exception e) {
        }
        return def;
    }
