    public synchronized boolean append(TxnHeader hdr, Record txn) throws IOException {
        if (hdr != null) {
            if (hdr.getZxid() <= lastZxidSeen) {
                LOG.warn("Current zxid " + hdr.getZxid() + " is <= " + lastZxidSeen + " for " + hdr.getType());
            }
            if (logStream == null) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Creating new log file: log." + Long.toHexString(hdr.getZxid()));
                }
                logFileWrite = new File(logDir, ("log." + Long.toHexString(hdr.getZxid())));
                fos = new FileOutputStream(logFileWrite);
                logStream = new BufferedOutputStream(fos);
                oa = BinaryOutputArchive.getArchive(logStream);
                FileHeader fhdr = new FileHeader(TXNLOG_MAGIC, VERSION, dbId);
                fhdr.serialize(oa, "fileheader");
                logStream.flush();
                currentSize = fos.getChannel().position();
                streamsToFlush.add(fos);
            }
            padFile(fos);
            byte[] buf = Util.marshallTxnEntry(hdr, txn);
            if (buf == null || buf.length == 0) {
                throw new IOException("Faulty serialization for header " + "and txn");
            }
            Checksum crc = makeChecksumAlgorithm();
            crc.update(buf, 0, buf.length);
            oa.writeLong(crc.getValue(), "txnEntryCRC");
            Util.writeTxnBytes(oa, buf);
            return true;
        }
        return false;
    }
