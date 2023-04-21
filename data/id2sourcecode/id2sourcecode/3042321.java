    private static OutputStream[] openFilters(OutputStream s, String[] filters) {
        OutputStream[] os;
        if ((filters != null) && (filters.length != 0)) {
            os = new OutputStream[filters.length + 1];
            os[os.length - 1] = s;
            for (int i = os.length - 2; i >= 0; i--) {
                if (filters[i].equals("ASCIIHex")) {
                    os[i] = new ASCIIHexOutputStream(os[i + 1]);
                } else if (filters[i].equals("ASCII85")) {
                    os[i] = new ASCII85OutputStream(os[i + 1]);
                } else if (filters[i].equals("Flate")) {
                    os[i] = new FlateOutputStream(os[i + 1]);
                } else if (filters[i].equals("DCT")) {
                    os[i] = os[i + 1];
                } else {
                    System.err.println("PDFWriter: unknown stream format: " + filters[i]);
                }
            }
        } else {
            os = new OutputStream[1];
            os[0] = s;
        }
        return os;
    }
