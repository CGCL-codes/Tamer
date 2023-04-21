    public static ValuePair<cr_guid, cr_soundness_proof> checkReq(Logger logger, RpcCall req, long start_time_us, MessageDigest md, KeyFactory keyFactory, Signature verifyEngine) {
        final String method_tag = StorageSetCreator.class.getName() + ".checkReq";
        cr_guid extent_key = null;
        cr_soundness_proof latest_proof = null;
        switch(req.proc.getProcNum()) {
            case cr_get_latest_config_1:
                {
                    cr_get_latest_config_args args = (cr_get_latest_config_args) req.args;
                    extent_key = args.extent_key;
                }
                break;
            case cr_renew_1:
                {
                    cr_renew_args args = (cr_renew_args) req.args;
                    cr_signed_certificate cert = args.cert;
                    cr_signed_ss_set_config latest_config = args.latest_config;
                    assert cert.cert.expire_time > (start_time_us / 1000L) : "cert.expire_time=" + cert.cert.expire_time + "ms < now=" + (start_time_us / 1000L) + "ms";
                    assert latest_config.config.expire_time > (start_time_us / 1000L) : "latest_config.config.expire_time=" + latest_config.config.expire_time + "ms < now=" + (start_time_us / 1000L) + "ms";
                    extent_key = latest_config.config.extent_key;
                    Map<BigInteger, cr_soundness_proof> sig_proofs = null;
                    SortedMap<KeyTriple<Long, Long, BigInteger>, cr_soundness_proof> latest_proofs = null;
                    assert !AntiquityUtils.equals(latest_config.config.public_key, CR_NULL_PUBLIC_KEY) && !AntiquityUtils.equals(latest_config.config.extent_key, CR_NULL_GUID) && !AntiquityUtils.equals(latest_config.config.client_id, CR_NULL_GUID) && verifySignature(latest_config.config, latest_config.signature, latest_config.config.public_key, keyFactory, verifyEngine) : method_tag + ": Verification failed for latest_config" + XdrUtils.toString(latest_config);
                    for (cr_soundness_proof proof : args.proof) {
                        assert XdrUtils.equals(extent_key, proof.config.config.extent_key) : "Extent key=" + XdrUtils.toString(extent_key) + " does not match proof.config.extent_key=" + XdrUtils.toString(proof.config.config.extent_key);
                        assert checkSoundnessProof(proof, true, false, md, keyFactory, verifyEngine) : "Invalid proof " + XdrUtils.toString(proof);
                        md.update(XdrUtils.serialize(proof.cert.cert));
                        md.update(XdrUtils.serialize(proof.config.config));
                        BigInteger hash = byteArrayToBigInteger(md.digest());
                        if (sig_proofs == null) {
                            sig_proofs = new LinkedHashMap<BigInteger, cr_soundness_proof>();
                            latest_proofs = new TreeMap<KeyTriple<Long, Long, BigInteger>, cr_soundness_proof>();
                        }
                        if (!sig_proofs.containsKey(hash)) {
                            sig_proofs.put(hash, proof);
                            KeyTriple<Long, Long, BigInteger> key = new KeyTriple<Long, Long, BigInteger>(proof.config.config.seq_num, proof.cert.cert.seq_num, hash);
                            assert !latest_proofs.containsKey(key) : method_tag + ": proofs map already contains key " + key + ".  value=" + XdrUtils.toString(latest_proofs.get(key)) + ", but want to put " + XdrUtils.toString(proof);
                            latest_proofs.put(key, proof);
                        }
                    }
                    int num_repair_sigs = 0;
                    for (int i = 0; i < args.sig.length; i++) {
                        BigInteger hash = guidToBigInteger(args.sig_proof_hash[i]);
                        cr_soundness_proof proof = sig_proofs.get(hash);
                        assert proof != null : "proof is null for" + " repair_args.sig[" + i + "]=" + XdrUtils.toString(args.sig[i]) + " repair_args.sig_proof_hash[" + i + "]=0x" + GuidTools.guid_to_string(hash);
                        assert verifyRepairSignature(logger, args.sig[i], proof, latest_config, md, keyFactory, verifyEngine) : "Invalid repair_args.sig[" + i + "]=" + XdrUtils.toString(args.sig[i]) + " repair_args.sig_proof_hash[" + i + "]=0x" + GuidTools.guid_to_string(hash) + " proof=" + XdrUtils.toString(proof);
                        num_repair_sigs++;
                    }
                    assert (latest_config.config.threshold - 1) % 2 == 0 : "low_watermark=" + latest_config.config.threshold + " is not an odd number";
                    int f = (latest_config.config.threshold - 1) / 2;
                    assert num_repair_sigs >= (f + 1) : method_tag + ":not enough valid repair signatures. " + num_repair_sigs + " < (f=" + f + "+1)=" + (f + 1);
                    KeyTriple<Long, Long, BigInteger> latestProofKey = latest_proofs.lastKey();
                    latest_proof = latest_proofs.get(latestProofKey);
                    cr_signed_certificate cert_cloned = XdrUtils.clone(cert);
                    cert_cloned.cert.timestamp = latest_proof.cert.cert.timestamp;
                    cert_cloned.cert.expire_time = latest_proof.cert.cert.expire_time;
                    cert_cloned.cert.seq_num = latest_proof.cert.cert.seq_num;
                    cert_cloned.cert.user_data = latest_proof.cert.cert.user_data;
                    cert_cloned.cert.holes = latest_proof.cert.cert.holes;
                    cert_cloned.signature = latest_proof.cert.signature;
                    assert XdrUtils.equals(cert_cloned, latest_proof.cert) : "(modified) cloned_cert=" + XdrUtils.toString(cert_cloned) + " != latest_proof.cert=" + XdrUtils.toString(latest_proof.cert) + ". cloned_cert is a mixture of new_cert=" + XdrUtils.toString(cert) + " and latest_proof.cert";
                    assert (cert.cert.seq_num > latest_proof.cert.cert.seq_num) && (cert.cert.timestamp > latest_proof.cert.cert.timestamp) && (cert.cert.expire_time >= latest_proof.cert.cert.expire_time) : " new cert " + XdrUtils.toString(cert) + " is not later than old cert" + XdrUtils.toString(latest_proof.cert);
                }
                break;
            case cr_repair_1:
                {
                    cr_repair_args args = (cr_repair_args) req.args;
                    cr_signed_ss_set_config latest_config = args.latest_config;
                    assert latest_config.config.expire_time > (start_time_us / 1000L) : "latest_config.config.expire_time=" + latest_config.config.expire_time + "ms < now=" + (start_time_us / 1000L) + "ms";
                    extent_key = latest_config.config.extent_key;
                    Map<BigInteger, cr_soundness_proof> sig_proofs = null;
                    SortedMap<KeyTriple<Long, Long, BigInteger>, cr_soundness_proof> latest_proofs = null;
                    assert !AntiquityUtils.equals(latest_config.config.public_key, CR_NULL_PUBLIC_KEY) && !AntiquityUtils.equals(latest_config.config.extent_key, CR_NULL_GUID) && !AntiquityUtils.equals(latest_config.config.client_id, CR_NULL_GUID) && verifySignature(latest_config.config, latest_config.signature, latest_config.config.public_key, keyFactory, verifyEngine) : method_tag + ": Verification failed for latest_config" + XdrUtils.toString(latest_config);
                    for (cr_soundness_proof proof : args.proof) {
                        assert XdrUtils.equals(extent_key, proof.config.config.extent_key) : "Extent key=" + XdrUtils.toString(extent_key) + " does not match proof.config.extent_key=" + XdrUtils.toString(proof.config.config.extent_key);
                        assert checkSoundnessProof(proof, true, false, md, keyFactory, verifyEngine) : "Invalid proof " + XdrUtils.toString(proof);
                        md.update(XdrUtils.serialize(proof.cert.cert));
                        md.update(XdrUtils.serialize(proof.config.config));
                        BigInteger hash = byteArrayToBigInteger(md.digest());
                        if (sig_proofs == null) {
                            sig_proofs = new LinkedHashMap<BigInteger, cr_soundness_proof>();
                            latest_proofs = new TreeMap<KeyTriple<Long, Long, BigInteger>, cr_soundness_proof>();
                        }
                        if (!sig_proofs.containsKey(hash)) {
                            sig_proofs.put(hash, proof);
                            KeyTriple<Long, Long, BigInteger> key = new KeyTriple<Long, Long, BigInteger>(proof.config.config.seq_num, proof.cert.cert.seq_num, hash);
                            assert !latest_proofs.containsKey(key) : method_tag + ": proofs map already contains key " + key + ".  value=" + XdrUtils.toString(latest_proofs.get(key)) + ", but want to put " + XdrUtils.toString(proof);
                            latest_proofs.put(key, proof);
                        }
                    }
                    int num_repair_sigs = 0;
                    for (int i = 0; i < args.sig.length; i++) {
                        BigInteger hash = guidToBigInteger(args.sig_proof_hash[i]);
                        cr_soundness_proof proof = sig_proofs.get(hash);
                        assert proof != null : "proof is null for" + " repair_args.sig[" + i + "]=" + XdrUtils.toString(args.sig[i]) + " repair_args.sig_proof_hash[" + i + "]=0x" + GuidTools.guid_to_string(hash);
                        assert verifyRepairSignature(logger, args.sig[i], proof, latest_config, md, keyFactory, verifyEngine) : "Invalid repair_args.sig[" + i + "]=" + XdrUtils.toString(args.sig[i]) + " repair_args.sig_proof_hash[" + i + "]=0x" + GuidTools.guid_to_string(hash) + " proof=" + XdrUtils.toString(proof);
                        num_repair_sigs++;
                    }
                    assert (latest_config.config.threshold - 1) % 2 == 0 : "low_watermark=" + latest_config.config.threshold + " is not an odd number";
                    int f = (latest_config.config.threshold - 1) / 2;
                    assert num_repair_sigs >= (f + 1) : method_tag + ": not enough valid repair signatures. " + num_repair_sigs + " < (f=" + f + "+1)=" + (f + 1);
                    KeyTriple<Long, Long, BigInteger> latestProofKey = latest_proofs.lastKey();
                    latest_proof = latest_proofs.get(latestProofKey);
                }
                break;
            case cr_create_ss_set_1:
                {
                    cr_create_ss_set_args args = (cr_create_ss_set_args) req.args;
                    cr_signed_certificate cert = args.cert;
                    assert cert.cert.expire_time > (start_time_us / 1000L) : "cert.expire_time=" + cert.cert.expire_time + "ms < now=" + (start_time_us / 1000L) + "ms";
                    assert verifySignature(args.cert.cert, args.cert.signature, args.cert.cert.public_key, keyFactory, verifyEngine) : method_tag + ": Verification failed for " + XdrUtils.toString(args.cert);
                    extent_key = (cert.cert.type == CR_KEY_VERIFIED ? computeGuid(cert.cert.public_key, cr_guid.class, md) : cert.cert.verifier);
                }
                break;
            default:
                assert false : "checkReq: received an unregistered remote procedured call.";
                break;
        }
        return new ValuePair<cr_guid, cr_soundness_proof>(extent_key, latest_proof);
    }
