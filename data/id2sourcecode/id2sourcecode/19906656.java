    private void addAttribute(String name, byte[] b) {
        int index = -1;
        if (ATTR_SUPERCLASS.equals(name) && superClassNameIndex > 0) {
            index = superClassNameIndex;
        }
        if (ATTR_MEMBER.equals(name) && memberNameIndex > 0) {
            index = memberNameIndex;
        }
        if (ATTR_INTERFACES.equals(name) && interfacesNameIndex > 0) {
            index = interfacesNameIndex;
        }
        if (index == -1) {
            index = addConstant(name);
        }
        byte[] res = new byte[arr.length + b.length + 6];
        System.arraycopy(arr, 0, res, 0, arr.length);
        writeU2(res, arr.length, index);
        writeU4(res, arr.length + 2, b.length);
        int begin = arr.length + 6;
        System.arraycopy(b, 0, res, begin, b.length);
        atEnd += b.length + 6;
        writeU2(res, atCount, readU2(atCount) + 1);
        arr = res;
    }
