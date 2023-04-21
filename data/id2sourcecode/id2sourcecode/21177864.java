    TerminalHandler(String type) {
        this.type = type;
        String urlString = new StringBuffer("/org/jbuzz/telnet/terminal/").append(type).append(".xml").toString();
        URL url = TerminalHandler.class.getResource(urlString);
        InputStream inputStream = null;
        Document document = null;
        try {
            inputStream = url.openStream();
            document = documentBuilder.parse(inputStream);
        } catch (Exception e) {
            return;
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
            }
        }
        Element documentElement = document.getDocumentElement();
        String extendType = documentElement.getAttribute("extends");
        this.keySequence = new KeySequence(documentElement, extendType);
        NodeList controlElements = documentElement.getElementsByTagName("control");
        int controlElementLength = controlElements.getLength();
        if (extendType.length() > 0) {
            TerminalHandler terminalHandler = TerminalHandler.getInstance(extendType);
            this.controls = new HashMap<String, Control>(terminalHandler.controls.size() + (controlElementLength >> 1));
            for (Control control : terminalHandler.controls.values()) {
                this.controls.put(control.name, control);
            }
        } else {
            this.controls = new HashMap<String, Control>(controlElementLength);
        }
        for (int i = 0; i < controlElementLength; i++) {
            Element controlElement = (Element) controlElements.item(i);
            String name = controlElement.getAttribute("name");
            String output = controlElement.getAttribute("output");
            if (output.length() > 0) {
                String input = controlElement.getAttribute("input");
                if (input.length() == 0) {
                    this.controls.put(name, new Control(name, output));
                } else {
                    this.controls.put(name, new Control(name, output, input));
                }
            }
        }
        this.getCursorControl = this.controls.get("getCursor");
        this.setCursorControl = this.controls.get("setCursor");
        this.setCursorUpControl = this.controls.get("setCursorUp");
        this.setCursorDownControl = this.controls.get("setCursorDown");
        this.setCursorRightControl = this.controls.get("setCursorRight");
        this.setCursorLeftControl = this.controls.get("setCursorLeft");
        this.saveCursorControl = this.controls.get("saveCursor");
        this.restoreCursorControl = this.controls.get("restoreCursor");
        this.scrollScreenUpControl = this.controls.get("scrollScreenUp");
        this.scrollScreenDownControl = this.controls.get("scrollScreenDown");
        this.scrollScreenRightControl = this.controls.get("scrollScreenRight");
        this.scrollScreenLeftControl = this.controls.get("scrollScreenLeft");
        this.eraseScreenControl = this.controls.get("eraseScreen");
        this.eraseLineControl = this.controls.get("eraseLine");
    }
