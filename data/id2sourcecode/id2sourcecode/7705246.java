    public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) throws HibernateException, SQLException {
        final Object object;
        final InputStream inputStream = resultSet.getBinaryStream(names[0]);
        if (inputStream == null) {
            object = null;
        } else {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                final byte[] buffer = new byte[65536];
                int read = -1;
                while ((read = inputStream.read(buffer)) > -1) {
                    outputStream.write(buffer, 0, read);
                }
                outputStream.close();
            } catch (IOException exception) {
                throw new HibernateException("Unable to read blob " + names[0], exception);
            }
            object = outputStream.toByteArray();
        }
        return object;
    }
