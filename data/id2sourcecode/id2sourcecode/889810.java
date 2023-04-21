    private void getChannelList(Document doc, Element formEl, String chanMatch) {
        Element option = null;
        Text text = null;
        boolean chanExists = false;
        HashMap<String, Channel> channels = store.getChannels();
        String[] keys = (String[]) channels.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        for (int x = 0; x < keys.length; x++) {
            option = doc.createElement("option");
            text = doc.createTextNode(keys[x]);
            option.appendChild(text);
            formEl.appendChild(option);
            if (keys[x].equals(chanMatch)) chanExists = true;
        }
        if (chanMatch != null) {
            if (!chanExists) {
                option = doc.createElement("option");
                text = doc.createTextNode(chanMatch);
                option.appendChild(text);
                formEl.appendChild(option);
            }
            if (".*".compareTo(chanMatch) != 0) {
                option = doc.createElement("option");
                text = doc.createTextNode(".*");
                option.appendChild(text);
                formEl.appendChild(option);
            }
        }
    }
