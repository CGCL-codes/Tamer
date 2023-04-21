    @Override
    public void openScript(URL url) {
        try {
            URLConnection urlConn = url.openConnection();
            InputStream input = new BufferedInputStream(urlConn.getInputStream());
            global_currentScript = Script.loadFromXML(input);
        } catch (Exception e) {
            ReactivePlotUtils.displayExceptionErrorBox(e, this);
            return;
        }
        String scriptName = global_currentScript.info.getTitle();
        this.setTitle(scriptName + " - " + global_applicationName);
        global_currentScriptURL = url;
        getCurrentScriptMenu().setEnabled(true);
        global_structureViewer.setScript(global_currentScript);
        Node start = global_structureViewer.getCurrentNodeList().getStartNode();
        global_textEditor.setScript(global_currentScript, start);
        updateDisplayForChangedSelectedRoute();
        scrollToNode(start, start);
    }
