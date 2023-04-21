    private static void writeFields(PrintWriter pw, Hashtable fieldsread, String[] fields) {
        for (int i = 0; i < fields.length; i++) {
            pw.write((i > 0 ? "\t" : "") + fieldsread.get(fields[i]));
            System.out.print((i > 0 ? "\t" : "") + fieldsread.get(fields[i]));
        }
        pw.println();
    }
