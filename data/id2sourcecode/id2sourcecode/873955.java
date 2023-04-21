    private byte[] showAddEditForm(HTTPurl urlData) throws Exception {
        String name = urlData.getParameter("ID");
        HashMap channels = store.getChannels();
        Channel chan = null;
        if (name != null) {
            chan = (Channel) channels.get(name);
        }
        PageTemplate template = new PageTemplate(store.getProperty("path.template").replace('\\', File.separatorChar) + File.separator + "channel-details.html");
        if (chan != null) {
            template.replaceAll("$name", chan.getName());
            template.replaceAll("$chanOldName", chan.getName());
            template.replaceAll("$frequency", new Integer(chan.getFrequency()).toString());
            template.replaceAll("$bandwidth", new Integer(chan.getBandWidth()).toString());
            template.replaceAll("$programid", new Integer(chan.getProgramID()).toString());
            template.replaceAll("$videopid", new Integer(chan.getVideoPid()).toString());
            template.replaceAll("$audiopid", new Integer(chan.getAudioPid()).toString());
            String audioType = "";
            if (chan.getAudioType() == Channel.TYPE_AUDIO_MPG) {
                audioType = "<option value=\"1\" selected>MPG</option>\n" + "<option value=\"2\">AC3</option>\n";
            } else {
                audioType = "<option value=\"1\">MPG</option>\n" + "<option value=\"2\" selected>AC3</option>\n";
            }
            template.replaceAll("$audioType", audioType);
            template.replaceAll("$captureType", getCapTypeList(chan.getCaptureType()));
        } else {
            template.replaceAll("$name", "");
            template.replaceAll("$frequency", "");
            template.replaceAll("$bandwidth", "");
            template.replaceAll("$programid", "");
            template.replaceAll("$videopid", "");
            template.replaceAll("$audiopid", "");
            template.replaceAll("$chanOldName", "");
            String audioType = "<option value=\"1\" selected>MPG</option>\n" + "<option value=\"2\">AC3</option>\n";
            template.replaceAll("$audioType", audioType);
        }
        return template.getPageBytes();
    }
