    void buildArrayType() {
        String[] array_methods_none = { "append", "buffer_info", "byteswap", "extend", "fromfile", "fromlist", "fromstring", "fromunicode", "index", "insert", "pop", "read", "remove", "reverse", "tofile", "tolist", "typecode", "write" };
        for (String m : array_methods_none) {
            BaseArray.getTable().update(m, newLibUrl("array"), newFunc(None), METHOD);
        }
        String[] array_methods_num = { "count", "itemsize" };
        for (String m : array_methods_num) {
            BaseArray.getTable().update(m, newLibUrl("array"), newFunc(BaseNum), METHOD);
        }
        String[] array_methods_str = { "tostring", "tounicode" };
        for (String m : array_methods_str) {
            BaseArray.getTable().update(m, newLibUrl("array"), newFunc(BaseStr), METHOD);
        }
    }
