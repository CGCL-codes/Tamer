    public static MyTree parse(URL url, Hashtable<String, Integer> treeToAlignment) {
        try {
            return DNDParser.parse(url.openStream(), treeToAlignment);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
