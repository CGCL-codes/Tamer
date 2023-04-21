    @Override
    public Object init() throws Exception {
        if (url != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            bsh = new Interpreter();
            bsh.eval(reader);
            script = (ScriptTag) bsh.getInterface(ScriptTag.class);
            reader.close();
            initDeclaredMethods(bsh);
            return bsh;
        } else return null;
    }
