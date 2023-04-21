    public boolean computeValue(STUNMessage stunMessage) {
        try {
            MessageDigest md5;
            md5 = MessageDigest.getInstance("MD5");
            byte[] key = md5.digest("rstrzele:pjwstk.edu.pl:33600".getBytes());
            logger.debug(Arrays.byteArrayToHexString(key));
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
            byte[] message = new byte[this.messagePosition / 8];
            logger.debug("Mesage size = " + stunMessage.toByte().length + " Hmac message size = " + this.messagePosition / 8);
            System.arraycopy(stunMessage.toByte(), 0, message, 0, this.messagePosition / 8);
            Mac mac = Mac.getInstance("HmacSHA1");
            logger.debug(Arrays.byteArrayToHexString(message));
            mac.init(signingKey);
            byte[] result = mac.doFinal(message);
            if (result.length != 20) {
                logger.debug("Incorrect message integrity length " + result + "(" + result.length + ")");
                return false;
            }
            ExtendedBitSet resultBS = new ExtendedBitSet(20 * 8, false);
            resultBS.set(0, result);
            attribute.set(32, resultBS);
            setLength(20);
            logger.debug("Changed MESSAGE-INTEGRITY attribute to " + this.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
