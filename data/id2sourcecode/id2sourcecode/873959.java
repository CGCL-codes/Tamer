    private byte[] deleteChannel(HTTPurl urlData) throws Exception {
        GuideStore guide = GuideStore.getInstance();
        String name = urlData.getParameter("ID");
        if (name != null) {
            store.removeChannel(name);
            store.saveChannels(null);
            boolean save = false;
            Vector chanMap = guide.getChannelMap();
            for (int x = 0; x < chanMap.size(); x++) {
                String[] map = (String[]) chanMap.get(x);
                if (map[0].equals(name)) {
                    chanMap.remove(x);
                    x--;
                    save = true;
                }
            }
            if (save) guide.saveChannelMap(null);
        }
        StringBuffer out = new StringBuffer(256);
        out.append("HTTP/1.0 302 Moved Temporarily\n");
        out.append("Location: /servlet/" + urlData.getServletClass() + "\n\n");
        return out.toString().getBytes();
    }
