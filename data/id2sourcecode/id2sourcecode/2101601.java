    @Override
    public void save(ISessionData session, List<String> dirtyAttributes, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (session != null) {
                CookieDataSupport cookieData = new CookieDataSupport(session);
                cookieData.setRemoteAddress(getFullRemoteAddr(request));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                OutputStream outputStream = baos;
                if (compress) {
                    outputStream = new GZIPOutputStream(outputStream);
                }
                ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                oos.writeObject(cookieData);
                oos.close();
                outputStream.close();
                baos.close();
                byte[] data;
                try {
                    Cipher encryptCipher = Cipher.getInstance(ENCRYPTION_WITH_PARAM);
                    encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
                    data = encryptCipher.doFinal(baos.toByteArray());
                } catch (Exception e) {
                    throw new IOException(e.getMessage());
                }
                byte[] size = (data.length + SEPARATOR).getBytes();
                setCookieData(request, response, ArrayUtils.addAll(size, data));
                if (logger.isDebugEnabled()) {
                    logger.debug("Cookie size : " + ArrayUtils.addAll(size, data).length);
                }
            } else {
                setCookieData(request, response, null);
            }
        } catch (SignatureException e) {
            throw new IOException(e);
        }
    }
