    public synchronized boolean present(Screen sc) throws Exception {
        if (sc.get_GiScreened().containsKey(gi)) {
            boolean b = (sc.get_GiScreened().get(gi)).equals("");
            if (!b) {
                bb[num_frame][num_sub_sequence].write("The gi: " + gi + " has already been scanned before. The result was: it's a Select Agent.");
                bb[num_frame][num_sub_sequence].newLine();
            } else {
                bb[num_frame][num_sub_sequence].write("The gi: " + gi + " has already been scanned before. The result was: it's a NOT Select Agent.");
                bb[num_frame][num_sub_sequence].newLine();
            }
            SA = sc.get_GiScreened().get(gi);
            return !b;
        }
        String inf = getInfo();
        boolean res = compare(inf, sc);
        String ishit = "";
        if (res) {
            ishit = SA;
        }
        sc.get_GiScreened().put(gi, ishit);
        return res;
    }
