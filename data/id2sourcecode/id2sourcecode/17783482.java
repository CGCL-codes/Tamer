    public boolean test() {
        String[] lines = null;
        try {
            lines = readStream(url.openStream());
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
        if (expected.length != lines.length) {
            System.err.println(url + ":\n  line count mismatch - " + lines.length + ", expected " + expected.length);
            return false;
        }
        for (int i = 0; i < expected.length; i++) if (!lines[i].equals(expected[i])) {
            System.err.println(url + ": mismatch on line " + (i + 1) + ":-");
            System.err.println("  " + expected[i]);
            System.err.println("  " + lines[i]);
            return false;
        }
        return true;
    }
