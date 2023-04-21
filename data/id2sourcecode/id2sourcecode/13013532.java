    public InputSource resolveEntity(String publicId, String systemId) {
        InputSource inputSource = null;
        InputStream is = null;
        URL url = null;
        try {
            if (publicId == null) {
                if (systemId != null) {
                    if (QUARTZ_SCHEMA.equals(systemId)) {
                        is = getClass().getResourceAsStream(QUARTZ_DTD);
                    } else {
                        is = getInputStream(systemId);
                        if (is == null) {
                            int start = systemId.indexOf(QUARTZ_SYSTEM_ID_PREFIX);
                            if (start > -1) {
                                String fileName = systemId.substring(QUARTZ_SYSTEM_ID_PREFIX.length());
                                is = getInputStream(fileName);
                            } else {
                                if (systemId.indexOf(':') == -1) {
                                    File file = new java.io.File(systemId);
                                    url = file.toURL();
                                } else {
                                    url = new URL(systemId);
                                }
                                is = url.openStream();
                            }
                        }
                    }
                }
            } else {
                if (QUARTZ_PUBLIC_ID.equals(publicId)) {
                    is = getClass().getResourceAsStream(QUARTZ_DTD);
                } else {
                    url = new URL(systemId);
                    is = url.openStream();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                inputSource = new InputSource(is);
                inputSource.setPublicId(publicId);
                inputSource.setSystemId(systemId);
            }
        }
        return inputSource;
    }
