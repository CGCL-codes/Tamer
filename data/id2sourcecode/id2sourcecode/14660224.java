    private void send_stop_config(int attempts, int renew_attempts, Extent<gw_public_key, gw_guid, gw_signed_certificate> ext) {
        final String method_tag = tag + ".send_stop_config";
        logger.info(method_tag + ": " + "Calling stop_config procedure w/ " + attempts + " attempts...");
        gw_guid extent_key = (ext.getCertificate().cert.type == GW_KEY_VERIFIED ? computeGuid(ext.getCertificate().cert.public_key, gw_guid.class, _md) : ext.getCertificate().cert.verifier);
        BigInteger extent_key_sha1 = guidToBigInteger(extent_key);
        gw_stop_config_args stop_config_args = new gw_stop_config_args();
        gw_soundness_proof proof = _proofs.get(extent_key_sha1);
        stop_config_args.latest_config = proof.config;
        try {
            stop_config_args.sig = new gw_signed_signature();
            stop_config_args.sig.public_key = _ant_pkey;
            _md.update(XdrUtils.serialize(stop_config_args.latest_config.config));
            stop_config_args.sig.datahash = _md.digest();
            stop_config_args.sig.signature = createSignature(stop_config_args.sig.datahash, _skey, _sig_engine);
        } catch (Exception e) {
            BUG(method_tag + ": " + e);
        }
        dispatch(gway, stop_config_args, getProcedureKey(Procedure.GW_STOP_CONFIG), curry(stop_config_done, attempts, renew_attempts, ext));
    }
