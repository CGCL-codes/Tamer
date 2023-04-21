        public byte[] digest(MessageDigest md) {
            md.reset();
            if (oldStyle) {
                doOldStyle(md, rawBytes, offset, lengthWithBlankLine);
            } else {
                md.update(rawBytes, offset, lengthWithBlankLine);
            }
            return md.digest();
        }
