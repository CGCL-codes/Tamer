    private byte[] showChannels(HTTPurl urlData) throws Exception {
        StringBuffer out = new StringBuffer(4096);
        HashMap<String, Channel> channels = store.getChannels();
        String[] keys = (String[]) channels.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        Channel ch = null;
        for (int x = 0; x < keys.length; x++) {
            ch = (Channel) channels.get(keys[x]);
            String channelName = "";
            try {
                channelName = URLEncoder.encode(keys[x], "UTF-8");
            } catch (Exception e) {
            }
            out.append("<tr>");
            out.append("<td nowrap><span class='channelName'>" + keys[x] + "</span></td>");
            out.append("<td class='channelInfo'>" + ch.getFrequency() + "</td>\n");
            out.append("<td class='channelInfo'>" + ch.getBandWidth() + "</td>\n");
            out.append("<td class='channelInfo'>" + ch.getProgramID() + "</td>\n");
            out.append("<td class='channelInfo'>" + ch.getVideoPid() + "</td>\n");
            out.append("<td class='channelInfo'>" + ch.getAudioPid());
            if (ch.getAudioType() == Channel.TYPE_AUDIO_AC3) out.append("-AC3"); else out.append("-MPG");
            out.append("</td>\n");
            if (ch.getCaptureType() == -1) {
                out.append("<td class='channelInfo'>AutoSelect</td>\n");
            } else {
                CaptureCapabilities caps = CaptureCapabilities.getInstance();
                CaptureCapability cap = caps.getCapabiltyWithID(ch.getCaptureType());
                String capName = "ERROR";
                if (cap != null) capName = caps.getCapabiltyWithID(ch.getCaptureType()).getName();
                out.append("<td class='channelInfo'>" + capName + "</td>\n");
            }
            out.append("<td class='channelInfo'>");
            out.append(" <a onClick='return confirmAction(\"Delete\");' href='/servlet/" + urlData.getServletClass() + "?action=06&ID=" + channelName + "'>");
            out.append("<img border=0 src='/images/delete.png' alt='Delete Channel' align='absmiddle' width='24' height='24'>");
            out.append("</a>");
            out.append(" <a href='/servlet/" + urlData.getServletClass() + "?action=09&ID=" + channelName + "'>");
            out.append("<img border=0 src='/images/edit.png' alt='Edit Channel' align='absmiddle' width='24' height='24'>");
            out.append("</a> ");
            out.append("</td>\n");
            out.append("<tr>");
        }
        PageTemplate template = new PageTemplate(store.getProperty("path.template").replace('\\', File.separatorChar) + File.separator + "channels.html");
        template.replaceAll("$channels", out.toString());
        return template.getPageBytes();
    }
