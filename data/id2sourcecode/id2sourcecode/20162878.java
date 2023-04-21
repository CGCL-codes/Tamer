    protected String buildAttributesMethods(PogoString pgs, int lang) throws PogoException, IOException, FileNotFoundException {
        String tab = (lang == javaLang) ? "\t\t" : "\t";
        int start, end, ptr;
        if (lang == javaLang) {
            if ((start = pgs.str.indexOf(startAttrStr)) < 0) throw new PogoException("Input File Syntax error !\n" + startAttrStr + "\nNot Found !");
            if ((end = pgs.str.indexOf(endAttrStr)) < 0) throw new PogoException("Input File Syntax error !\n" + endAttrStr + "\n Not found !");
            String prev_declar = pgs.str.substring(start, end);
            StringBuffer sb = new StringBuffer(startAttrStr);
            sb.append("\n\n");
            for (int i = 0; i < pogo.attributes.size(); i++) {
                Attrib attr = pogo.attributes.attributeAt(i);
                sb.append(attr.javaMemberData());
            }
            sb.append("\n");
            pgs.replace(prev_declar, sb.toString());
        }
        String rwAttr;
        if (lang == javaLang) rwAttr = pogo.templates_dir + "/java/ReadWriteAttr.java"; else rwAttr = pogo.templates_dir + "/cpp/ReadHardwareAttr.cpp";
        String read = readAttributeTemplate(rwAttr, readFile);
        String write = readAttributeTemplate(rwAttr, writeFile);
        String pattern;
        if (lang == javaLang) pattern = "public void read_attr_hardware"; else pattern = "::read_attr_hardware";
        if (pgs.str.indexOf(pattern) < 0) {
            if (lang == javaLang) pattern = "always_executed_hook()"; else pattern = "::always_executed_hook";
            if ((ptr = pgs.str.indexOf(pattern)) < 0) throw new PogoException("\'" + pattern + "()\' method not found");
            ptr = pgs.inMethod(ptr) + 1;
            ptr = pgs.outMethod(ptr) + 1;
            pgs.insert(ptr, read);
        }
        boolean writable = false;
        for (int i = 0; i < pogo.attributes.size(); i++) {
            Attrib attr = pogo.attributes.attributeAt(i);
            if (attr.getWritable()) writable = true;
        }
        if (lang == javaLang) pattern = "public void write_attr_hardware"; else pattern = "::write_attr_hardware";
        if (writable && pgs.str.indexOf(pattern) < 0) {
            if (lang == javaLang) pattern = "public void always_executed_hook()"; else pattern = "::always_executed_hook()";
            ptr = pgs.str.indexOf(pattern);
            ptr = pgs.inMethod(ptr) + 1;
            ptr = pgs.outMethod(ptr) + 1;
            pgs.insert(ptr, write);
        }
        if (lang == javaLang) pattern = "public void read_attr("; else pattern = "::read_attr(";
        if ((start = pgs.str.indexOf(pattern)) < 0) throw new PogoException("\'" + pattern + ")\' method not found");
        start = pgs.inMethod(start) + 1;
        end = pgs.outMethod(start);
        end = pgs.str.lastIndexOf('}', end);
        PogoString method = new PogoString(pgs.str.substring(start, end));
        String oldmethod = method.str;
        if ((ptr = method.str.indexOf("Switch on attribute name")) < 0) {
            System.out.println(method);
            throw new PogoException("\'Switch on attribute name\' not found");
        }
        ptr = method.nextCr(ptr) + 1;
        ptr = method.nextCr(ptr) + 1;
        for (int i = 0; i < pogo.attributes.size(); i++) {
            Attrib attr = pogo.attributes.attributeAt(i);
            if (attr.rwType != ATTR_WRITE) {
                String target = "\"" + attr.name + "\"";
                if (method.str.indexOf(target, ptr) < 0) {
                    String str = "";
                    if (i != 0) str += tab + "else\n";
                    str += tab + "if (attr_name == " + target + ")\n" + tab + "{\n" + tab + "\t//	Add your own code here\n" + tab + "}\n";
                    int here;
                    if (i != 0) here = method.str.lastIndexOf("}", method.str.length()) + "}\n".length(); else here = ptr;
                    method.insert(here, str);
                }
            }
        }
        pgs.replace(oldmethod, method.str);
        if (writable) {
            if (lang == javaLang) pattern = "public void write_attr_hardware("; else pattern = "::write_attr_hardware";
            if ((start = pgs.str.indexOf(pattern)) < 0) throw new PogoException("\'" + pattern + "\' method not found");
            start = pgs.inMethod(start) + 1;
            end = pgs.outMethod(start) - 2;
            method = new PogoString(pgs.str.substring(start, end));
            oldmethod = method.str;
            if ((ptr = method.str.indexOf("Switch on attribute name")) < 0) throw new PogoException("\'Switch on attribute name\' not found");
            ptr = method.nextCr(ptr) + 1;
            ptr = method.nextCr(ptr) + 1;
            for (int i = 0, n = 0; i < pogo.attributes.size(); i++) {
                Attrib attr = pogo.attributes.attributeAt(i);
                tab = (lang == javaLang) ? "\t\t\t" : "\t\t";
                if (attr.getWritable()) {
                    String target = "\"" + attr.name + "\"";
                    if (method.str.indexOf(target, ptr) < 0) {
                        String str = "";
                        if (n != 0) str += tab + "else\n";
                        str += tab + "if (attr_name == " + target + ")\n" + tab + "{\n" + tab + "\t//	Add your own code here\n" + tab + "}\n";
                        int here;
                        if (n != 0) {
                            here = method.str.lastIndexOf("}", method.str.length());
                            here = method.str.lastIndexOf("}", here - 1) + "}\n".length();
                        } else here = ptr;
                        method.insert(here, str);
                    }
                    n++;
                }
            }
            pgs.replace(oldmethod, method.str);
        }
        return pgs.toString();
    }
