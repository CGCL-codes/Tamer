    private int testOTP(String msg, String[] challenge) {
        String[] lines = Utilities.split(msg, "\r\n");
        for (int i = 0; i < lines.length; i++) {
            String[] t = Utilities.split(lines[i], " ");
            for (int k = 1; k < t.length; k++) {
                try {
                    if (Pattern.matches("[0-9]+", t[k])) {
                        challenge[0] = t[k];
                        challenge[1] = t[k + 1];
                        if (lines[i].indexOf("md4") != -1) return 4; else return 5;
                    }
                } catch (Exception e) {
                }
            }
        }
        return -1;
    }
