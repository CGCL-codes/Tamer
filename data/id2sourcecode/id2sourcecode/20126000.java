    private String generateSignature(List params, String secretKey) {
        StringBuffer buffer = new StringBuffer();
        Collections.sort(params);
        for (Iterator it = params.iterator(); it.hasNext(); ) {
            buffer.append(it.next());
        }
        buffer.append(secretKey);
        StringBuffer result = new StringBuffer();
        byte[] digest = createDigester().digest(buffer.toString().getBytes());
        for (int i = 0; i < digest.length; i++) {
            byte b = digest[i];
            result.append(Integer.toHexString((b & 0xf0) >>> 4));
            result.append(Integer.toHexString(b & 0x0f));
        }
        return result.toString();
    }
