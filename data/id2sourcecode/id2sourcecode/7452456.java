        @Override
        protected void engineUpdate(byte[] b, int off, int len) throws SignatureException {
            LOG.debug("engineUpdate(b,off,len): off=" + off + "; len=" + len);
            this.messageDigest.update(b, off, len);
            byte[] digestValue = this.messageDigest.digest();
            SHA1WithRSASignature.digestValues.set(digestValue);
        }
