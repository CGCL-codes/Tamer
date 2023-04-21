    public static int register(Context context, int assetstore, String bitstreamPath) throws SQLException, IOException {
        String sInternalId = REGISTERED_FLAG + bitstreamPath;
        TableRow bitstream;
        Context tempContext = null;
        try {
            tempContext = new Context();
            bitstream = DatabaseManager.create(tempContext, "Bitstream");
            bitstream.setColumn("deleted", true);
            bitstream.setColumn("internal_id", sInternalId);
            bitstream.setColumn("store_number", assetstore);
            DatabaseManager.update(tempContext, bitstream);
            tempContext.complete();
        } catch (SQLException sqle) {
            if (tempContext != null) {
                tempContext.abort();
            }
            throw sqle;
        }
        GeneralFile file = getFile(bitstream);
        if (file instanceof LocalFile) {
            DigestInputStream dis = null;
            try {
                dis = new DigestInputStream(FileFactory.newFileInputStream(file), MessageDigest.getInstance("MD5"));
            } catch (NoSuchAlgorithmException e) {
                log.warn("Caught NoSuchAlgorithmException", e);
                throw new IOException("Invalid checksum algorithm");
            } catch (IOException e) {
                log.error("File: " + file.getAbsolutePath() + " to be registered cannot be opened - is it " + "really there?");
                throw e;
            }
            final int BUFFER_SIZE = 1024 * 4;
            final byte[] buffer = new byte[BUFFER_SIZE];
            while (true) {
                final int count = dis.read(buffer, 0, BUFFER_SIZE);
                if (count == -1) {
                    break;
                }
            }
            bitstream.setColumn("checksum", Utils.toHex(dis.getMessageDigest().digest()));
            dis.close();
        } else if (file instanceof SRBFile) {
            if (!file.exists()) {
                log.error("File: " + file.getAbsolutePath() + " is not in SRB MCAT");
                throw new IOException("File is not in SRB MCAT");
            }
            int iLastSlash = bitstreamPath.lastIndexOf('/');
            String sFilename = bitstreamPath.substring(iLastSlash + 1);
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                log.error("Caught NoSuchAlgorithmException", e);
                throw new IOException("Invalid checksum algorithm");
            }
            bitstream.setColumn("checksum", Utils.toHex(md.digest(sFilename.getBytes())));
        } else {
            throw new IOException("Unrecognized file type - " + "not local, not SRB");
        }
        bitstream.setColumn("checksum_algorithm", "MD5");
        bitstream.setColumn("size_bytes", file.length());
        bitstream.setColumn("deleted", false);
        DatabaseManager.update(context, bitstream);
        int bitstream_id = bitstream.getIntColumn("bitstream_id");
        if (log.isDebugEnabled()) {
            log.debug("Stored bitstream " + bitstream_id + " in file " + file.getAbsolutePath());
        }
        return bitstream_id;
    }
