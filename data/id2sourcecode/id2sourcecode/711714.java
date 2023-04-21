    private void getChannelList(Document doc, Element formEl) {
        Element option = null;
        Text text = null;
        HashMap<String, Channel> channels = store.getChannels();
        String[] keys = (String[]) channels.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        option = doc.createElement("option");
        text = doc.createTextNode("Any");
        option.appendChild(text);
        formEl.appendChild(option);
        for (int x = 0; x < keys.length; x++) {
            option = doc.createElement("option");
            text = doc.createTextNode(keys[x]);
            option.appendChild(text);
            formEl.appendChild(option);
        }
    }
