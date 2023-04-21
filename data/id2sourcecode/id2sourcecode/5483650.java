    String updatePyAttributes(String code, String readcode, String template, String class_name) throws PogoException {
        String target = "attr.set_value(";
        int start = template.indexOf(target);
        if (start < 0) throw new PogoException("Attribute template syntax error.");
        start = template.indexOf("\n", start) + 1;
        String read_templ = template.substring(0, start);
        int idx = start;
        target = "#\tAdd your own code here";
        start = template.indexOf(target, start);
        if (start < 0) throw new PogoException("Attribute template syntax error.");
        start += target.length() + 1;
        String write_templ = template.substring(idx, start);
        String stm_templ = "\t" + template.substring(start).trim();
        int insert_pos = code.indexOf("def read_attr_hardware(self,");
        insert_pos = PogoUtil.endOfPythonMethod(code, insert_pos);
        code = code.substring(0, insert_pos) + "\n\n" + code.substring(insert_pos);
        insert_pos += 2;
        for (int i = size() - 1; i >= 0; i--) {
            Attrib attr = attributeAt(i);
            String read_method;
            String signature = "def read_" + attr.name + "(self, attr):";
            if (readcode.indexOf(signature) > 0) {
                read_method = PogoUtil.pythonMethod(readcode, signature);
            } else read_method = attr.buildPyReadMethod(read_templ);
            read_method = "\n" + read_method.trim() + "\n\n";
            String write_method = null;
            if (attr.rwType == ATTR_WRITE || attr.rwType == ATTR_READ_WRITE) {
                signature = "def write_" + attr.name + "(self, attr):";
                if (readcode.indexOf(signature) > 0) {
                    write_method = PogoUtil.pythonMethod(readcode, signature);
                } else write_method = attr.buildPyWriteMethod(write_templ);
                write_method = "\n" + write_method.trim() + "\n\n";
            }
            String stm_method = null;
            signature = "def is_" + attr.name + "_allowed(self, req_type):";
            int sigpos = readcode.indexOf(signature);
            if (sigpos > 0 || attr.notAllowedFor.size() > 0) {
                if (sigpos > 0) {
                    stm_method = PogoUtil.pythonMethod(readcode, signature);
                } else stm_method = attr.buildPyStateMachineMethod(stm_templ);
                stm_method = attr.pyUpdateAllowedStates(stm_method, signature);
                stm_method = "\n" + stm_method.trim() + "\n\n";
            }
            if (stm_method != null) code = code.substring(0, insert_pos) + stm_method + code.substring(insert_pos);
            if (write_method != null) code = code.substring(0, insert_pos) + write_method + code.substring(insert_pos);
            code = code.substring(0, insert_pos) + read_method + code.substring(insert_pos);
        }
        return code;
    }
