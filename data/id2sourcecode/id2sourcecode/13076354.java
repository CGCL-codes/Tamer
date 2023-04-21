    public String generatePassword(String secret, Handler handler) throws Exception {
        Message msg = null;
        Bundle b = null;
        ;
        try {
            if (charLimit >= 0 && iterations > 0) {
                byte[] result = site.concat(secret).getBytes(theApp.getTextEncoding());
                MessageDigest internalHasher = null;
                Digest bcHasher = null;
                if (hash.compareTo("MD5") == 0 || hash.compareTo("SHA-1") == 0 || hash.compareTo("SHA-256") == 0 || hash.compareTo("SHA-384") == 0 || hash.compareTo("SHA-512") == 0) {
                    internalHasher = MessageDigest.getInstance(hash);
                } else if (hash.compareTo("RIPEMD-160") == 0) {
                    bcHasher = new RIPEMD160Digest();
                } else if (hash.compareTo("Tiger") == 0) {
                    bcHasher = new TigerDigest();
                } else if (hash.compareTo("Whirlpool") == 0) {
                    bcHasher = new WhirlpoolDigest();
                }
                if (internalHasher != null) {
                    for (int i = 0; i < iterations; i++) {
                        result = internalHasher.digest(result);
                        if (handler != null) {
                            msg = handler.obtainMessage();
                            b = new Bundle();
                            b.putInt("iteration", i);
                            b.putString("password", null);
                            msg.setData(b);
                            handler.sendMessage(msg);
                        }
                    }
                } else if (bcHasher != null) {
                    for (int i = 0; i < iterations; i++) {
                        bcHasher.update(result, 0, result.length);
                        result = new byte[bcHasher.getDigestSize()];
                        bcHasher.doFinal(result, 0);
                        bcHasher.reset();
                        if (handler != null) {
                            msg = handler.obtainMessage();
                            b = new Bundle();
                            b.putInt("iteration", i);
                            b.putString("password", null);
                            msg.setData(b);
                            handler.sendMessage(msg);
                        }
                    }
                }
                if (result != null) {
                    String b64hash = base64String(result);
                    Pattern p = null;
                    switch(charTypes) {
                        case 1:
                            p = Pattern.compile("\\W");
                            b64hash = p.matcher(b64hash).replaceAll("_");
                            break;
                        case 2:
                            p = Pattern.compile("[^a-zA-Z0-9]");
                            b64hash = p.matcher(b64hash).replaceAll("");
                            break;
                        case 3:
                            p = Pattern.compile("[^a-zA-Z]");
                            b64hash = p.matcher(b64hash).replaceAll("");
                            break;
                        case 4:
                            p = Pattern.compile("\\D");
                            b64hash = p.matcher(b64hash).replaceAll("");
                            break;
                        default:
                            break;
                    }
                    if (charLimit > 0 && b64hash.length() > charLimit) b64hash = b64hash.substring(0, charLimit);
                    if (handler != null) {
                        msg = handler.obtainMessage();
                        b = new Bundle();
                        b.putInt("iteration", -100);
                        b.putString("password", b64hash);
                        msg.setData(b);
                        handler.sendMessage(msg);
                    }
                    return b64hash;
                } else {
                    throw new Exception(theApp.getResources().getString(R.string.error_null_hash));
                }
            } else {
                if (iterations <= 0) throw new Exception(theApp.getResources().getString(R.string.error_bad_iterations)); else if (charLimit < 0) throw new Exception(theApp.getResources().getString(R.string.error_bad_charlimit)); else throw new Exception(theApp.getResources().getString(R.string.error_unknown));
            }
        } catch (Exception e) {
            throw e;
        }
    }
