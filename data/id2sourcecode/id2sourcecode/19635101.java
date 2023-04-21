    private String[] splitUrl(String url) {
        String[] parts = url.split("/");
        if (parts.length == 0) return null;
        if (!parts[0].equalsIgnoreCase("")) return parts;
        if (parts.length <= 1) return null;
        String[] retParts = new String[parts.length - 1];
        for (int i = 0; i < retParts.length; i++) {
            retParts[i] = parts[i + 1];
        }
        return retParts;
    }
