    public String generateEntryKey(String key, HttpServletRequest request, int scope, String language) {
        StringBuffer cBuffer = new StringBuffer(AVERAGE_KEY_LENGTH);
        if (language != null) {
            cBuffer.append(FILE_SEPARATOR).append(language);
        }
        if (key != null) {
            cBuffer.append(FILE_SEPARATOR).append(key);
        } else {
            String generatedKey = request.getRequestURI();
            if (generatedKey.charAt(0) != FILE_SEPARATOR_CHAR) {
                cBuffer.append(FILE_SEPARATOR_CHAR);
            }
            cBuffer.append(generatedKey);
            cBuffer.append("_").append(request.getMethod()).append("_");
            generatedKey = getSortedQueryString(request);
            if (generatedKey != null) {
                try {
                    java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                    byte[] b = digest.digest(generatedKey.getBytes());
                    cBuffer.append("_");
                    cBuffer.append(toBase64(b).replace('/', '_'));
                } catch (Exception e) {
                }
            }
        }
        return cBuffer.toString();
    }
