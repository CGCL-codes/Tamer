    public void readVariables(Resource resource, Variables writeTo) {
        Hashtable variables = new Hashtable();
        String text = resource.getText();
        Perl5Util matcher = new Perl5Util();
        if (matcher.match("/" + getRegex() + "/", text)) {
            String value = matcher.group(1);
            writeTo.set(getVariableName(), value);
        } else {
            writeTo.set(getVariableName(), null);
        }
    }
