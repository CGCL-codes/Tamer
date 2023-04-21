    public Object typeCast(Object value) throws TypeCastException {
        logger.debug("typeCast(value={}) - start", value);
        if (value == null || value == ITable.NO_VALUE) {
            return null;
        }
        if (value instanceof byte[]) {
            return value;
        }
        if (value instanceof String) {
            String stringValue = (String) value;
            if (stringValue.length() == 0 || stringValue.length() > MAX_URI_LENGTH) {
                logger.debug("Assuming given string to be Base64 and not a URI");
                return Base64.decode((String) value);
            }
            try {
                logger.debug("Assuming given string to be a URI");
                try {
                    URL url = new URL(stringValue);
                    return toByteArray(url.openStream(), 0);
                } catch (MalformedURLException e1) {
                    logger.debug("Given string is not a valid URI - trying to resolve it as file...");
                    try {
                        File file = new File(stringValue);
                        return toByteArray(new FileInputStream(file), (int) file.length());
                    } catch (FileNotFoundException e2) {
                        logger.debug("Assuming given string to be Base64 and not a URI or File");
                        return Base64.decode(stringValue);
                    }
                }
            } catch (IOException e) {
                throw new TypeCastException(value, this, e);
            }
        }
        if (value instanceof Blob) {
            try {
                Blob blobValue = (Blob) value;
                if (blobValue.length() == 0) {
                    return null;
                }
                return blobValue.getBytes(1, (int) blobValue.length());
            } catch (SQLException e) {
                throw new TypeCastException(value, this, e);
            }
        }
        if (value instanceof URL) {
            try {
                return toByteArray(((URL) value).openStream(), 0);
            } catch (IOException e) {
                throw new TypeCastException(value, this, e);
            }
        }
        if (value instanceof File) {
            try {
                File file = (File) value;
                return toByteArray(new FileInputStream(file), (int) file.length());
            } catch (IOException e) {
                throw new TypeCastException(value, this, e);
            }
        }
        throw new TypeCastException(value, this);
    }
