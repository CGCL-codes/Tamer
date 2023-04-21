    public boolean parseRequest(HttpServletRequest request) throws IOException, ServletException {
        sis = request.getInputStream();
        StringWriter sw = new StringWriter();
        sw.write("\r\n");
        int i = readByte();
        for (; i != -1 && i != '\r'; i = readByte()) sw.write(i);
        String delimiter = sw.toString();
        int dellength = delimiter.length();
        readByte();
        while (true) {
            StringWriter h = new StringWriter();
            int[] temp = new int[4];
            temp[0] = readByte();
            temp[1] = readByte();
            temp[2] = readByte();
            h.write(temp[0]);
            h.write(temp[1]);
            h.write(temp[2]);
            for (temp[3] = readByte(); temp[3] != -1; temp[3] = readByte()) {
                if (temp[0] == '\r' && temp[1] == '\n' && temp[2] == '\r' && temp[3] == '\n') break;
                h.write(temp[3]);
                temp[0] = temp[1];
                temp[1] = temp[2];
                temp[2] = temp[3];
            }
            String header = h.toString();
            int startName = header.indexOf("name=\"");
            int endName = header.indexOf("\"", startName + 6);
            if (startName == -1 || endName == -1) break;
            String name = header.substring(startName + 6, endName);
            String value = "";
            startName = header.indexOf("filename=\"", endName + 1);
            if (startName > 0) {
                endName = header.indexOf("\"", startName + 10);
                value = header.substring(startName + 10, endName);
                int slash = value.lastIndexOf('\\');
                if (slash != -1) value = value.substring(slash + 1);
            }
            del_l = 0;
            if (!value.equals("")) {
                int ind = readFirstByteOfFile(name);
                while (ind != -1) {
                    if (buffer[ind] == delimiter.charAt(del_l)) {
                        if (++del_l == dellength) break;
                    } else if (del_l > 0) {
                        if (buffer[ind] == '\r') del_l = 1; else del_l = 0;
                    }
                    ind = readByteOfFile();
                }
                writeLastByteOfFile();
            } else {
                int b = readByte();
                int ib = 0;
                while (b != -1) {
                    if (b == delimiter.charAt(del_l)) {
                        if (++del_l == dellength) break;
                    } else if (del_l == 0) bValue[ib++] = (byte) b; else {
                        for (int ii = 0; ii < del_l; ii++) {
                            bValue[ib++] = (byte) delimiter.charAt(ii);
                        }
                        if (b == '\r') del_l = 1; else {
                            del_l = 0;
                            bValue[ib++] = (byte) b;
                        }
                    }
                    b = readByte();
                }
                value = new String(bValue, 0, ib, request.getCharacterEncoding());
            }
            if (!userParameters.containsKey(name)) userParameters.put(name, value);
            if (readByte() == '-' && readByte() == '-') break;
        }
        return true;
    }
