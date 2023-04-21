    public void sendFiles(List files) throws IOException {
        logger.debug("sendFiles starting");
        int i;
        int phase = 0;
        while (true) {
            int offset = 0;
            i = in.readInt();
            logger.debug("read file index " + i);
            if (i == -1) {
                if (phase == 0 && remoteVersion >= 13) {
                    phase++;
                    config.strongSumLength = SUM_LENGTH;
                    out.writeInt(-1);
                    out.flush();
                    logger.debug("sendFiles phase=" + phase);
                    continue;
                }
                break;
            }
            if (i < 0 || i >= files.size()) {
                String msg = "invalid file index " + i + " (count=" + files.size() + ")";
                logger.fatal(msg);
                throw new IOException(msg);
            }
            FileInfo finfo = (FileInfo) files.get(i);
            File file = new File(finfo.filename());
            stats.num_transferred_files++;
            stats.total_transferred_size += file.length();
            if (phase == 0) stats.total_size += file.length();
            logger.info(finfo.filename());
            List sums = receiveSums();
            out.writeInt(i);
            out.writeInt(count);
            out.writeInt(n);
            out.writeInt(remainder);
            out.flush();
            config.blockLength = n;
            MatcherStream match = new MatcherStream(config);
            match.setChecksums(sums);
            match.addListener(this);
            deltasOut = new PlainDeltaEncoder(config, out);
            DigestInputStream fin = null;
            try {
                MessageDigest md = MessageDigest.getInstance("BrokenMD4");
                md.update(config.checksumSeed);
                byte[] buf = new byte[CHUNK_SIZE];
                fin = new DigestInputStream(new FileInputStream(file), md);
                int len;
                while ((len = fin.read(buf)) != -1) {
                    logger.debug("updating matcher with " + len + " bytes");
                    match.update(buf, 0, len);
                    out.flush();
                }
                match.doFinal();
                deltasOut.doFinal();
                byte[] digest = md.digest();
                logger.debug("file_sum=" + Util.toHexString(digest));
                out.write(digest);
                out.flush();
                fin.close();
            } catch (ListenerException le) {
                throw (IOException) le.getCause();
            } catch (NoSuchAlgorithmException nsae) {
                throw new IOException("could not create message digest");
            }
        }
        out.writeInt(-1);
        out.flush();
        logger.debug("sendFiles finished");
    }
