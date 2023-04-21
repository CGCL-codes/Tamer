    private void eval(Element element, URL url) throws IOException, ScriptException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        this.scriptEngine.put("printDoc", new PrintStream(os));
        InputStreamReader isr = new InputStreamReader(url.openStream());
        this.scriptEngine.eval(isr);
        isr.close();
        os.close();
        String str = os.toString();
        System.out.println("load: " + url);
        this.scriptEngine.put("printDoc", null);
        if ((str != null) && (str.length() > 0) && (element != null)) {
            try {
                setParser(new ParserDelegator());
                insertAfterEnd(element, str);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }
