    private String buildAttributesMethods(PogoString code) {
        int insert_pos = code.indexOf(pogo.class_name + "::always_executed_hook()");
        insert_pos = code.inMethod(insert_pos);
        insert_pos = code.outMethod(insert_pos);
        String signature = pogo.attributes.readHardwareSignatureMethod(pogo.class_name);
        int start;
        if ((start = code.indexOf(signature)) < 0) {
            String method = pogo.attributes.readHardwareFullSignatureMethod(pogo.class_name);
            method += "\n{\n" + PogoUtil.enteringTrace(signature) + "	//	Add your own code here\n" + "}\n\n";
            code.insert(insert_pos, method);
            insert_pos += method.length();
        } else {
            insert_pos = code.inMethod(start);
            insert_pos = code.outMethod(insert_pos);
        }
        for (int i = 0; i < pogo.attributes.size(); i++) {
            Attrib attr = pogo.attributes.attributeAt(i);
            signature = attr.readSignatureMethod(pogo.class_name);
            if (code.indexOf(signature) < 0) {
                String method = attr.readFullSignatureMethod(pogo.class_name);
                method += "\n{\n" + PogoUtil.enteringTrace(signature);
                method += attr.getDevImpl2ReadAttr(code, pogo.class_name);
                method += "}\n\n";
                code.insert(insert_pos, method);
                insert_pos += method.length();
            }
            if (attr.getWritable()) {
                signature = attr.writeSignatureMethod(pogo.class_name);
                if (code.indexOf(signature) < 0) {
                    String method = attr.writeFullSignatureMethod(pogo.class_name);
                    method += "\n{\n" + PogoUtil.enteringTrace(signature);
                    method += attr.getDevImpl2WriteAttr(code, pogo.class_name);
                    method += "}\n\n";
                    code.insert(insert_pos, method);
                    insert_pos += method.length();
                }
            }
        }
        comment_read_write_attr_methods(code, pogo.class_name);
        return code.str;
    }
