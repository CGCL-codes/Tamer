    private void loadLM(File arpa) throws NumberFormatException, IOException {
        if (!arpa.exists()) {
            return;
        }
        BufferedReader rd = new BufferedReader(new FileReader(arpa.getAbsolutePath()));
        String str;
        int order = 100;
        String[] typeBuffer = null;
        while ((str = rd.readLine()) != null) {
            if (str.length() == 0) {
                continue;
            }
            if (str.charAt(0) == '\\') {
                String p = str.substring(1, 2);
                try {
                    order = Integer.parseInt(p);
                } catch (Throwable e) {
                    continue;
                }
                if (order == 0) continue;
                typeBuffer = new String[order];
                continue;
            }
            String[] slots = str.split("\\s+");
            if (slots.length > order) {
                double prob = Double.parseDouble(slots[0]);
                for (int i = 0; i < order; i++) {
                    typeBuffer[i] = slots[i + 1];
                }
                double bow = 0;
                if (slots.length > order + 1) {
                    bow = Double.parseDouble(slots[order + 1]);
                }
                insertNgram(typeBuffer, prob / log10e, bow / log10e);
            }
        }
        maxOrder = order;
    }
