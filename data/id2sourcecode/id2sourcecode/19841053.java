    private static String[] getChannelList() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Ch");
        for (int i = 1; i <= 13; i++) list.add(Integer.toString(i));
        return list.toArray(new String[list.size()]);
    }
