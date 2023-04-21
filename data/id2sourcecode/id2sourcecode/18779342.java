    public gw_soundness_proof getLatestProof() {
        gw_signed_ss_set_config latest_config = getLatestConfig();
        if (_versionMapLog != null) {
            for (Map<BigInteger, gw_log_entry> logEntries : _versionMapLog.values()) {
                if (logEntries.size() >= latest_config.config.threshold) {
                    gw_soundness_proof proof = createProofFromLog(extent_key, logEntries.values(), gw_soundness_proof.class, _md);
                    if (proof != null) {
                        _md.update(XdrUtils.serialize(proof.cert.cert));
                        _md.update(XdrUtils.serialize(proof.config.config));
                        BigInteger hash = byteArrayToBigInteger(_md.digest());
                        KeyTriple<Long, Long, BigInteger> proofKey = new KeyTriple<Long, Long, BigInteger>(proof.config.config.seq_num, proof.cert.cert.seq_num, hash);
                        if (!_versionMapProof.containsKey(proofKey)) _versionMapProof.put(proofKey, proof);
                    }
                }
            }
        }
        KeyTriple<Long, Long, BigInteger> latestProofKey = (_versionMapProof != null ? _versionMapProof.lastKey() : null);
        gw_soundness_proof latest_proof = (latestProofKey != null ? _versionMapProof.get(latestProofKey) : null);
        return latest_proof;
    }
