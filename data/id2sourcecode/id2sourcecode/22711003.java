    private String getToken(long tokenTime) {
        getMd().reset();
        byte[] bs = getMd().digest((getId() + String.valueOf(tokenTime) + getId()).getBytes());
        StringBuilder key = new StringBuilder();
        boolean t = tokenTime % 2 == 0;
        for (byte b : bs) {
            if (t = !t) {
                continue;
            }
            key.append(Math.abs(b % 10));
            if (key.length() >= TOKEN_LENGTH) {
                break;
            }
        }
        return key.substring(0, TOKEN_LENGTH);
    }
