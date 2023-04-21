    private byte[] deleteChannelMap(HTTPurl urlData) throws Exception {
        GuideStore guide = GuideStore.getInstance();
        int id = -1;
        try {
            id = Integer.parseInt(urlData.getParameter("id"));
        } catch (Exception e) {
        }
        Vector<String[]> chanMap = guide.getChannelMap();
        if (id > -1 && id < chanMap.size()) {
            chanMap.remove(id);
            guide.saveChannelMap(null);
        }
        StringBuffer buff = new StringBuffer();
        buff.append("HTTP/1.0 302 Moved Temporarily\n");
        buff.append("Location: /servlet/EpgDataRes?action=04\n\n");
        return buff.toString().getBytes();
    }
