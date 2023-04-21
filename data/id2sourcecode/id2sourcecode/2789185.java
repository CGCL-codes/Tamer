        private boolean validChecksum() {
            if (_digest != null) {
                String checksum = toChecksum(_digest.digest());
                if (_entry.hasChecksum() && !checksum.equals(_entry.getExtractedChecksum())) {
                    return false;
                }
            }
            return true;
        }
