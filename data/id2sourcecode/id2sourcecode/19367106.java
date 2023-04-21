    static Blob toBlob(@Nullable final Object x, final int jdbcType, final TdsConnectionObject caller) throws SQLException {
        if (x == null) return null; else if (x instanceof Blob) return (Blob) x; else if (x instanceof byte[]) return new TdsBlob(caller.tdsConnection(), (byte[]) x); else if (x instanceof Clob) {
            final Clob clob = (Clob) x;
            try {
                final Reader in = clob.getCharacterStream();
                final TdsBlob blob = new TdsBlob(caller.tdsConnection());
                final long length = clob.length();
                if (length > 0) {
                    OutputStreamXWriter out = new OutputStreamXWriter(blob.setBinaryStream(1), caller.tdsConnection().getCharset());
                    out.setCharBufferCapacity((int) Math.min(length, 8192));
                    out.transferFrom(in, length);
                    out.close();
                    out = null;
                    in.close();
                }
                return blob;
            } catch (final IOException ex) {
                throw new SQLDataException(Messages.get("error.generic.ioerror", ex.getMessage()), "HY000", ex);
            }
        } else if (x instanceof CharSequence) {
            final CharBuffer in = unsafeStringConstructor.asMaybeWritableCharBuffer((CharSequence) x);
            try {
                final TdsBlob blob = new TdsBlob(caller.tdsConnection());
                final int length = in.length();
                if (length > 0) {
                    final OutputStreamXWriter out = new OutputStreamXWriter(blob.setBinaryStream(1), caller.tdsConnection().getCharset());
                    out.transferFrom(in, length);
                    out.close();
                }
                return blob;
            } catch (final IOException ex) {
                throw new SQLNonTransientException(Messages.get("error.generic.ioerror", ex.getMessage()), "HY000", ex);
            }
        } else {
            throw new SQLDataException(Messages.get("error.convert.badtypes", x.getClass().getName(), getJdbcTypeName(jdbcType)), "22005");
        }
    }
