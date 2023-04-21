    void comment_read_write_attr_methods(PogoString code, String classname) {
        String pattern = "void " + classname + "::read_attr(Tango::Attribute &attr)";
        int start, end;
        if ((start = code.str.indexOf(pattern)) > 0) {
            end = code.inMethod(start);
            end = code.outMethod(end);
            code.insert(end, "#endif\n");
            code.insert(start, cpp_flag_dev_impl_2);
        }
        pattern = "void " + classname + "::write_attr_hardware(vector<long> &attr_list)";
        if ((start = code.str.indexOf(pattern)) > 0) {
            end = code.inMethod(start);
            end = code.outMethod(end);
            code.insert(end, "#endif\n");
            code.insert(start, cpp_flag_dev_impl_2);
        }
    }
