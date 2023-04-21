    void check(String pass) {
        m.update(pass.getBytes());
        byte dig[] = m.digest();
        for (int i = 0; i < 16; i++) {
            if (dig[i] == md5sum[i]) {
                if (i == 15) {
                    System.out.println(pass);
                    found = true;
                }
            } else break;
        }
    }
