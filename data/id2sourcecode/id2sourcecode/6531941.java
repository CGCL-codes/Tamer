    private void resolveAllProperties(Properties props) throws CompilerException {
        VariableSubstitutor subs = new VariableSubstitutor(props);
        subs.setBracesRequired(true);
        for (Enumeration e = props.keys(); e.hasMoreElements(); ) {
            String name = (String) e.nextElement();
            String value = props.getProperty(name);
            int mods = -1;
            do {
                StringReader read = new StringReader(value);
                StringWriter write = new StringWriter();
                try {
                    mods = subs.substitute(read, write, "at");
                    props.put(name, value);
                } catch (IOException ex) {
                    config.parseError(xmlProp, "Faild to load file: " + file.getAbsolutePath(), ex);
                }
            } while (mods != 0);
        }
    }
