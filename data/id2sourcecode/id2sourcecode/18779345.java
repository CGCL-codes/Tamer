    private boolean addRepairProofSig(gw_repair_args repair) {
        _md.update(XdrUtils.serialize(repair.latest_config.config));
        BigInteger hash = byteArrayToBigInteger(_md.digest());
        KeyTriple<Long, Long, BigInteger> latestConfigKey = new KeyTriple<Long, Long, BigInteger>(repair.latest_config.config.seq_num, repair.latest_config.config.timestamp, hash);
        if (_latestConfigMap == null) {
            _latestConfigMap = new TreeMap<KeyTriple<Long, Long, BigInteger>, gw_signed_ss_set_config>();
            _repairSignatureMap = new TreeMap<KeyTriple<Long, Long, BigInteger>, Map<BigInteger, ValuePair<gw_soundness_proof, gw_signed_signature>>>();
        }
        gw_signed_ss_set_config latest_config = _latestConfigMap.get(latestConfigKey);
        Map<BigInteger, ValuePair<gw_soundness_proof, gw_signed_signature>> repairSignatureMap = _repairSignatureMap.get(latestConfigKey);
        if (latest_config == null) {
            latest_config = repair.latest_config;
            _latestConfigMap.put(latestConfigKey, latest_config);
            repairSignatureMap = new HashMap<BigInteger, ValuePair<gw_soundness_proof, gw_signed_signature>>();
            _repairSignatureMap.put(latestConfigKey, repairSignatureMap);
        }
        assert XdrUtils.equals(latest_config, repair.latest_config) : "latest_config=" + XdrUtils.toString(latest_config) + " != repair.latest_config=" + XdrUtils.toString(repair.latest_config);
        _md.update(repair.sig.public_key.value);
        gw_guid sig_guid = new gw_guid(_md.digest());
        BigInteger sig_guid_sha1 = guidToBigInteger(sig_guid);
        assert getSSIndex(sig_guid, latest_config.config.ss_set, true) >= 0 : "H(sig.pk)=" + XdrUtils.toString(sig_guid) + " not contained in latest_config=" + XdrUtils.toString(latest_config);
        ValuePair<gw_soundness_proof, gw_signed_signature> repairSignature = repairSignatureMap.get(sig_guid_sha1);
        if (repairSignature == null) {
            repairSignature = new ValuePair<gw_soundness_proof, gw_signed_signature>(repair.proof, repair.sig);
            repairSignatureMap.put(sig_guid_sha1, repairSignature);
        } else {
            assert XdrUtils.equals(repair.proof, repairSignature.getFirst()) : "repair.proof=" + XdrUtils.toString(repair.proof) + " != repairSignature.proof=" + XdrUtils.toString(repairSignature.getFirst());
            assert XdrUtils.equals(repair.sig, repairSignature.getSecond()) : "repair.sig=" + XdrUtils.toString(repair.sig) + " != repairSignature.sig=" + XdrUtils.toString(repairSignature.getSecond());
        }
        return true;
    }
