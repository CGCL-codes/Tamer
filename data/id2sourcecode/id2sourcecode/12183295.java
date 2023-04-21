    @Test
    public void test() throws InternalServerErrorException {
        String username = "Mufasa";
        String realm = "testrealm@host.com";
        String password = "Circle Of Life";
        String nonce = "dcd98b7102dd2f0e8b11d0f600bfb0c093";
        String nonceCount = "00000001";
        String cnonce = "0a4f113b";
        String method = "GET";
        String digestUri = "/dir/index.html";
        RFC2617AuthQopDigest digestProcessor = new RFC2617AuthQopDigest(username, realm, password, nonce, nonceCount, cnonce, method, digestUri);
        String digest = digestProcessor.digest();
        assertTrue("Digest must not be nul and must have same value as the Response param in section 3.5 of RFC 2617, 6629fae49393a05397450978507c4ef1", digest != null && digest.equals("6629fae49393a05397450978507c4ef1"));
    }
