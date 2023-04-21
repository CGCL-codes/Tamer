    public String[] parseFile(File file) throws IOException {
        int magic;
        short majorVer;
        short minVer;
        short tableSz;
        byte tag;
        short strSz;
        String className;
        Map tableStrs = new HashMap();
        Stack classEntries = new Stack();
        Vector dependency = new Vector();
        ByteBuffer header = ByteBuffer.allocate(10);
        FileInputStream fis = new FileInputStream(file);
        header.order(ByteOrder.BIG_ENDIAN);
        fis.getChannel().read(header);
        header.rewind();
        magic = header.getInt();
        if (magic != 0xCAFEBABE) return (new String[0]);
        majorVer = header.getShort();
        minVer = header.getShort();
        if ((majorVer != 0) || (minVer < 46)) return (new String[0]);
        tableSz = header.getShort();
        for (int I = 1; I < tableSz; I++) {
            tag = (byte) fis.read();
            switch(tag) {
                case 1:
                    strSz = getShort(fis);
                    tableStrs.put(new Integer(I), new String(read(fis, strSz)));
                    break;
                case 7:
                    classEntries.push(new Integer(getShort(fis)));
                    break;
                case 8:
                    fis.skip(2);
                    break;
                case 3:
                case 4:
                case 9:
                case 10:
                case 11:
                case 12:
                    fis.skip(4);
                    break;
                case 5:
                case 6:
                    fis.skip(8);
                    break;
                default:
                    {
                        CPMake.debugPrint("JavaDependencyParser: Unknown tag " + tag + " at " + fis.getChannel().position());
                        I = tableSz;
                        break;
                    }
            }
        }
        fis.close();
        while (!classEntries.empty()) {
            className = (String) tableStrs.get((Integer) classEntries.pop());
            if (className != null) {
                dependency.add(className + ".java");
            }
        }
        return ((String[]) dependency.toArray(new String[0]));
    }
