    public int substitute(Reader reader, Writer writer, String type) throws IllegalArgumentException, IOException {
        int t = getTypeConstant(type);
        char variable_start = '$';
        char variable_end = '\0';
        if (t == TYPE_SHELL) {
            variable_start = '%';
        } else if (t == TYPE_AT) {
            variable_start = '@';
        } else if (t == TYPE_ANT) {
            variable_start = '@';
            variable_end = '@';
        }
        int subs = 0;
        int c = reader.read();
        while (true) {
            while (c != -1 && c != variable_start) {
                writer.write(c);
                c = reader.read();
            }
            if (c == -1) {
                return subs;
            }
            boolean braces = false;
            c = reader.read();
            if (c == '{') {
                braces = true;
                c = reader.read();
            } else if (bracesRequired) {
                writer.write(variable_start);
                continue;
            } else if (c == -1) {
                writer.write(variable_start);
                return subs;
            }
            StringBuffer nameBuffer = new StringBuffer();
            while (c != -1 && (braces && c != '}') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (braces && (c == '[') || (c == ']')) || (((c >= '0' && c <= '9') || c == '_' || c == '.' || c == '-') && nameBuffer.length() > 0)) {
                nameBuffer.append((char) c);
                c = reader.read();
            }
            String name = nameBuffer.toString();
            String varvalue = null;
            if (((!braces || c == '}') && (!braces || variable_end == '\0' || variable_end == c)) && name.length() > 0) {
                if (braces && name.startsWith("ENV[") && (name.lastIndexOf(']') == name.length() - 1)) {
                    varvalue = IoHelper.getenv(name.substring(4, name.length() - 1));
                } else {
                    varvalue = variables.getProperty(name);
                }
                subs++;
            }
            if (varvalue != null) {
                writer.write(escapeSpecialChars(varvalue, t));
                if (braces || variable_end != '\0') {
                    c = reader.read();
                }
            } else {
                writer.write(variable_start);
                if (braces) {
                    writer.write('{');
                }
                writer.write(name);
            }
        }
    }
