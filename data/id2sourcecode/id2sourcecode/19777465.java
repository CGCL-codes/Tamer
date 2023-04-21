        public HandshakeValidator() {
            bValid = false;
            SHA1Helper sha;
            try {
                sha = new SHA1Helper();
                validResponse = sha.digest(sessionID, secret);
            } catch (InstantiationException e) {
                throw new RuntimeException("Cannot create key value");
            }
        }
