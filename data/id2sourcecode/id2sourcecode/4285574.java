    private void send_renew_repair(boolean renew, cr_soundness_proof prev_proof) {
        final String method_tag = tag + ".send_" + (renew ? "renew" : "repair");
        logger.info(method_tag + ": " + "Calling " + (renew ? "renew" : "repair") + " procedure...");
        cr_signed_certificate cert = null;
        if (renew) {
            cert = XdrUtils.clone(prev_proof.cert);
            cert.cert.seq_num++;
            cert.cert.timestamp = now_ms();
            cert.cert.expire_time = cert.cert.timestamp + MAX_TTL * 1000L;
            try {
                cert.signature = createSignature(cert.cert, _skey, _sig_engine);
            } catch (Exception e) {
                BUG(method_tag + ": " + e);
            }
        }
        cr_ss_set_config proposed_config = XdrUtils.clone(prev_proof.config.config);
        cr_signed_ss_set_config latest_config = prev_proof.config;
        cr_soundness_proof[] proofs = new cr_soundness_proof[] { prev_proof };
        _md.update(serialize(prev_proof.cert.cert));
        _md.update(serialize(prev_proof.config.config));
        cr_guid sig_proof_hash = new cr_guid(_md.digest());
        cr_signed_signature[] sigs = new cr_signed_signature[prev_proof.config.config.ss_set_size];
        cr_guid[] sig_proof_hashes = new cr_guid[sigs.length];
        for (int i = 0; i < prev_proof.sigs.length; i++) {
            BigInteger gway_guid = guidToBigInteger(prev_proof.config.config.ss_set[i].guid);
            ValueTriple<cr_public_key, PrivateKey, Signature> gway_keys = _gwayKeys.get(gway_guid);
            assert gway_keys != null : "keys is null for gway_guid=0x" + GuidTools.guid_to_string(gway_guid);
            sigs[i] = createRepairSignature(prev_proof, prev_proof.config, gway_keys.getFirst(), cr_signed_signature.class, gway_keys.getSecond(), _md, _key_factory, gway_keys.getThird());
            sig_proof_hashes[i] = sig_proof_hash;
        }
        XdrAble args = null;
        if (renew) {
            cr_renew_args renew_args = new cr_renew_args();
            renew_args.cert = cert;
            renew_args.proposed_config = proposed_config;
            renew_args.latest_config = latest_config;
            renew_args.proof = proofs;
            renew_args.sig = sigs;
            renew_args.sig_proof_hash = sig_proof_hashes;
            args = renew_args;
        } else {
            cr_repair_args repair_args = new cr_repair_args();
            repair_args.proposed_config = proposed_config;
            repair_args.latest_config = latest_config;
            repair_args.proof = proofs;
            repair_args.sig = sigs;
            repair_args.sig_proof_hash = sig_proof_hashes;
            args = repair_args;
        }
        final boolean renew2 = renew;
        final Long start_time_us2 = now_us();
        final Long seq2 = new Long(_next_seq++);
        final RpcCall req = new RpcCall(gway, getProcedureKey(renew ? Procedure.CR_RENEW : Procedure.CR_REPAIR), args, appId, null, my_sink);
        req.userData = new Thunk1<RpcReply>() {

            public void run(RpcReply resp) {
                renew_repair_done(renew2, req, start_time_us2, seq2, resp);
            }
        };
        _pending_events.put(seq2, req);
        _acore.register_timer(TIMEOUT_MS, curry(timeout_cb, seq2));
        dispatch(req);
    }
