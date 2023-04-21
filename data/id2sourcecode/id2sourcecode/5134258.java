        public boolean authenticate(String username, String password, String uri, String nonce) {
            if (uri == null && nonce == null) {
                return DaapSettings.DAAP_PASSWORD.equals(password);
            } else if (uri != null && nonce != null) {
                String ha1 = DaapSettings.DAAP_PASSWORD.getValue();
                if (ha1.startsWith("MD5/")) {
                    ha1 = ha1.substring(4);
                }
                String ha2 = DaapUtil.calculateHA2(uri);
                String digest = DaapUtil.digest(ha1, ha2, nonce);
                return digest.equalsIgnoreCase(password);
            } else {
                if (LOG.isErrorEnabled()) {
                    LOG.error("Unknown scheme!");
                }
            }
            return false;
        }
