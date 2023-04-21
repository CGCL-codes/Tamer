    @Primitive
    public static Value caml_md5_chan(final CodeRunner ctxt, final Value channel, final Value len) throws Fail.Exception, FalseExit {
        try {
            final byte[] buffer = new byte[Md5.BUFFER_SIZE];
            final Channel ch = (Channel) channel.asBlock().asCustom();
            final MessageDigest md5 = MessageDigest.getInstance(Md5.ALGO);
            final int l = len.asLong();
            if (l < 0) {
                int nb = ch.read(buffer, 0, Md5.BUFFER_SIZE);
                while (nb != -1) {
                    md5.update(buffer, 0, nb);
                    nb = ch.read(buffer, 0, Md5.BUFFER_SIZE);
                }
            } else {
                int rem = l;
                while (rem > 0) {
                    final int nb = ch.read(buffer, 0, Math.min(buffer.length, rem));
                    if (nb == -1) {
                        Fail.raiseEndOfFile();
                    }
                    md5.update(buffer, 0, nb);
                    rem -= nb;
                }
            }
            return Value.createFromBlock(Block.createString(md5.digest()));
        } catch (final NoSuchAlgorithmException nsae) {
            Fail.invalidArgument("Digest.substring");
            return Value.UNIT;
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Sys.sysError(null, ioe.toString());
            return Value.UNIT;
        }
    }
