    public boolean addProof(gw_soundness_proof proof) {
        if (proof == null || !XdrUtils.equals(extent_key, proof.config.config.extent_key)) {
            if (_logger.isInfoEnabled()) _logger.info("RepairProofState.addProof: Extent key=" + XdrUtils.toString(extent_key) + " does not match repair_args.proof.config.extent_key=" + (proof == null ? null : XdrUtils.toString(proof.config.config.extent_key)));
            return false;
        }
        _md.update(XdrUtils.serialize(proof.cert.cert));
        _md.update(XdrUtils.serialize(proof.config.config));
        BigInteger hash = byteArrayToBigInteger(_md.digest());
        KeyTriple<Long, Long, BigInteger> versionKey = new KeyTriple<Long, Long, BigInteger>(proof.config.config.seq_num, proof.cert.cert.seq_num, hash);
        if (_versionMapProof == null) _versionMapProof = new TreeMap<KeyTriple<Long, Long, BigInteger>, gw_soundness_proof>();
        if (!_versionMapProof.containsKey(versionKey)) _versionMapProof.put(versionKey, proof);
        return true;
    }
