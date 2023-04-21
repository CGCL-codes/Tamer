    public String digest() throws InternalServerErrorException {
        if (logger.isDebugEnabled()) {
            logger.debug("Calculating RFC 2617 qop=auth digest with params: username = " + username + " , realm = " + realm + " , password = " + password + " , nonce = " + nonce + " , nonceCount = " + nonceCount + " , cnonce = " + cnonce + " , method = " + method + " , digestUri = " + digestUri + " ;");
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerErrorException("failed to get instance of MD5 digest, used in " + this.getClass());
        }
        final String a1 = username + ":" + realm + ":" + password;
        final String ha1 = AsciiHexStringEncoder.encode(messageDigest.digest(EncodingUtil.getAsciiBytes(a1)));
        final String a2 = method + ":" + digestUri;
        final String ha2 = AsciiHexStringEncoder.encode(messageDigest.digest(EncodingUtil.getAsciiBytes(a2)));
        final String kd = ha1 + ":" + nonce + ":" + nonceCount + ":" + cnonce + ":" + qop + ":" + ha2;
        return AsciiHexStringEncoder.encode(messageDigest.digest(EncodingUtil.getAsciiBytes(kd)));
    }
