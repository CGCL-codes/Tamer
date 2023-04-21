    public static void extractFromArc(String s) throws Exception {
        String fields = null;
        long offset = 0;
        String idxname = s;
        int jj = idxname.lastIndexOf(".");
        idxname = idxname.substring(0, jj);
        OutputStream out = new FileOutputStream(idxname + ".idxtmp");
        PrintWriter pw = new PrintWriter(out);
        boolean is_compressed = s.endsWith(".gz");
        if (fields == null) fields = (is_compressed ? "AebmngV" : "Aebmngv");
        String[] fieldarray = parseFields(fields);
        File f = new File(s);
        Hashtable fieldsread = new Hashtable();
        try {
            RandomAccessFile file = new RandomAccessFile(f, "r");
            try {
                do {
                    ARCInputStream in = new ARCInputStream(file, is_compressed, offset);
                    fieldsread.put("A", in.getUrl());
                    fieldsread.put("e", in.getIp());
                    fieldsread.put("b", in.getDate());
                    fieldsread.put("m", in.getMime());
                    fieldsread.put("n", in.getLength());
                    fieldsread.put(is_compressed ? "V" : "v", Long.toString(offset));
                    fieldsread.put("g", f.getName());
                    for (int i = 0; i < fieldarray.length; i++) {
                        pw.write((i > 0 ? "\t" : "") + fieldsread.get(fieldarray[i]));
                    }
                    pw.println();
                    in.readAll();
                    offset = file.getFilePointer();
                } while (offset < f.length());
            } finally {
                pw.flush();
                if (pw.checkError()) {
                    throw new Exception("problem with indexing");
                }
                pw.close();
                file.close();
            }
        } catch (IOException e) {
            System.out.println("Error reading from " + s + ": " + e);
        }
    }
