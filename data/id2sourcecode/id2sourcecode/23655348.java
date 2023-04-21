    public boolean fileCompare(String from, String to) throws FileManipulatorException {
        try {
            log.debug("Generate md5sum for " + from + " and " + to + " and compare it");
            FileObject fromFile = this.resolveFile(from);
            FileObject toFile = this.resolveFile(to);
            if (!fromFile.exists() || !toFile.exists()) {
                return true;
            }
            MessageDigest md = MessageDigest.getInstance("MD5");
            log.debug("MD5 message digest created");
            DigestInputStream fromStream = new DigestInputStream(fromFile.getContent().getInputStream(), md);
            byte[] fromBuffer = new byte[8192];
            while (fromStream.read(fromBuffer) != -1) ;
            byte[] fromMD5 = md.digest();
            md.reset();
            DigestInputStream toStream = new DigestInputStream(toFile.getContent().getInputStream(), md);
            byte[] toBuffer = new byte[8192];
            while (toStream.read(toBuffer) != -1) ;
            byte[] toMD5 = md.digest();
            return !MessageDigest.isEqual(fromMD5, toMD5);
        } catch (Exception e) {
            log.error("Files compare using MD5SUM error : " + e.getMessage());
            throw new FileManipulatorException(e.getMessage());
        }
    }
