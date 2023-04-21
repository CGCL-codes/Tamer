    private String getMapNameFromEntry(String entry) {
        Pattern pattern = Pattern.compile(GameDatabase.getMapPath(roomData.getChannel(), roomData.getModName()).replace('\\', '/') + "([\\p{Alnum}\\p{Punct}&&[^/\\\\]]+)\\." + GameDatabase.getMapExtension(roomData.getChannel(), roomData.getModName()).toLowerCase());
        Matcher matcher = pattern.matcher(entry.toLowerCase());
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }
