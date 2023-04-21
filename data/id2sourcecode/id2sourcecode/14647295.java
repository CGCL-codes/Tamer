    public static ArrayList<String> getChannelsWithNodes(Node node1, Node node2, Channels channels) {
        ArrayList<String> list = new ArrayList<String>();
        String channels1 = getChannelsConnectedTo(channels, node1);
        if (channels1 == null) {
            return null;
        }
        String[] list1 = channels1.split(",");
        String channels2 = getChannelsConnectedTo(channels, node2);
        if (channels2 == null) {
            return null;
        }
        String[] list2 = channels2.split(",");
        HashMap<String, String> commonList = new HashMap<String, String>();
        for (String element : list1) {
            commonList.put(element, element);
        }
        for (String element : list2) {
            if (commonList.containsKey(element)) {
                list.add(element);
            }
        }
        return list;
    }
