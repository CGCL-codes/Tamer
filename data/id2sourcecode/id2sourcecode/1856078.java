    private static void writeDataReading(HashMap<Transition, HashSet<DataItem>> datareading, BufferedWriter bw) throws IOException {
        bw.write("    <datareading>\n");
        for (Transition t : datareading.keySet()) {
            bw.write("        <reading>\n");
            bw.write("            <transition>tran_" + t.getNumber() + "</transition>\n");
            HashSet<DataItem> dis = datareading.get(t);
            for (DataItem di : dis) {
                bw.write("            <data>" + di.getId() + "</data>\n");
            }
            bw.write("        </reading>\n");
        }
        bw.write("    </datareading>");
        bw.newLine();
    }
