    public void getList(HTTPurl urlData, HashMap<String, String> headers, OutputStream out) throws Exception {
        XmlDoc doc = new XmlDoc("channels");
        HashMap<String, Channel> channels = store.getChannels();
        String[] keys = channels.keySet().toArray(new String[0]);
        Element chanElm = null;
        Element elm = null;
        for (int x = 0; x < keys.length; x++) {
            Channel chan = channels.get(keys[x]);
            chanElm = doc.createElement("channel");
            elm = doc.createTextElement("name", chan.getName());
            chanElm.appendChild(elm);
            elm = doc.createTextElement("frequency", new Integer(chan.getFrequency()).toString());
            chanElm.appendChild(elm);
            elm = doc.createTextElement("bandwidth", new Integer(chan.getBandWidth()).toString());
            chanElm.appendChild(elm);
            elm = doc.createTextElement("program_id", new Integer(chan.getProgramID()).toString());
            chanElm.appendChild(elm);
            elm = doc.createTextElement("video_id", new Integer(chan.getVideoPid()).toString());
            chanElm.appendChild(elm);
            elm = doc.createTextElement("audio_id", new Integer(chan.getAudioPid()).toString());
            chanElm.appendChild(elm);
            elm = doc.createTextElement("audio_type", new Integer(chan.getAudioType()).toString());
            chanElm.appendChild(elm);
            elm = doc.createTextElement("capture_type", new Integer(chan.getCaptureType()).toString());
            chanElm.appendChild(elm);
            doc.getRoot().appendChild(chanElm);
        }
        out.write(doc.getDocBytes());
    }
