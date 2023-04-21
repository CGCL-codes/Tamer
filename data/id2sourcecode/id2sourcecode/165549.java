    public void run() {
        char[] buf = new char[32];
        int chars_read;
        try {
            int b = sbr.read();
            out.write(b);
            while ((chars_read = sbr.read(buf)) != -1) out.write(buf, 0, chars_read);
            out.close();
        } catch (IOException e) {
            System.out.println("FAILED: Basic pipe test: " + e);
        }
    }
