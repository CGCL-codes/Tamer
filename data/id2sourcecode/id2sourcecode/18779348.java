    public boolean addLog(gw_log_entry log) {
        if (log == null || !XdrUtils.equals(extent_key, log.config.config.extent_key)) {
            if (_logger.isInfoEnabled()) _logger.info("RepairProofState.addLog: Extent key=" + XdrUtils.toString(extent_key) + " does not match log.config.extent_key=" + (log == null ? null : XdrUtils.toString(log.config.config.extent_key)));
            return false;
        }
        if (_versionMapProof != null && log.cert.cert.seq_num < _versionMapProof.lastKey().getFirst() && log.config.config.seq_num < _versionMapProof.lastKey().getSecond()) return false;
        _md.update(XdrUtils.serialize(log.cert.cert));
        _md.update(XdrUtils.serialize(log.config.config));
        BigInteger hash = byteArrayToBigInteger(_md.digest());
        KeyTriple<Long, Long, BigInteger> versionKey = new KeyTriple<Long, Long, BigInteger>(log.config.config.seq_num, log.cert.cert.seq_num, hash);
        if (_versionMapLog == null) _versionMapLog = new TreeMap<KeyTriple<Long, Long, BigInteger>, Map<BigInteger, gw_log_entry>>();
        Map<BigInteger, gw_log_entry> logEntryMap = _versionMapLog.get(versionKey);
        if (logEntryMap == null) {
            logEntryMap = new HashMap<BigInteger, gw_log_entry>();
            _versionMapLog.put(versionKey, logEntryMap);
        }
        _md.update(log.sig.public_key.value);
        gw_guid sig_guid = new gw_guid(_md.digest());
        BigInteger sig_guid_sha1 = guidToBigInteger(sig_guid);
        assert getSSIndex(sig_guid, log.config.config.ss_set, true) >= 0 : "H(log.sig.pk)=" + XdrUtils.toString(sig_guid) + " not contained in log.config=" + XdrUtils.toString(log.config);
        gw_log_entry logEntry = logEntryMap.get(sig_guid_sha1);
        if (logEntry == null) {
            logEntryMap.put(sig_guid_sha1, log);
        } else {
            assert XdrUtils.equals(log, logEntry) : "log=" + XdrUtils.toString(log) + " != logEntry=" + XdrUtils.toString(logEntry);
        }
        return true;
    }
