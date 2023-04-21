    private void send_ss_stop_config(NodeId ssGway, Extent<gw_public_key, gw_guid, gw_signed_certificate> ext, gw_soundness_proof proof, Set<NodeId> ssGways, Set<NodeId> doneSSGways) {
        final String method_tag = tag + ".send_ss_stop_config";
        logger.info(method_tag + ": " + "Calling ss_stop_config for extent " + XdrUtils.toString(proof.config.config.extent_key));
        Extent<gw_public_key, gw_guid, gw_signed_certificate> cloned_ext2 = null;
        try {
            cloned_ext2 = (Extent<gw_public_key, gw_guid, gw_signed_certificate>) ext.clone();
        } catch (Exception e) {
            BUG(method_tag + ": " + e);
        }
        Extent<gw_public_key, gw_guid, gw_signed_certificate> cloned_ext = cloned_ext2;
        gw_stop_config_args stop_config_args = new gw_stop_config_args();
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
        ss_stop_config_args stop_config_args_ss = (ss_stop_config_args) GatewayHelper.getSSArgs(stop_config_args, (gw_signed_ss_set_config) null, (gw_signed_ss_set_config) null, proof, getProcedureKey(Procedure.GW_STOP_CONFIG));
        dispatch(ssGway, stop_config_args_ss, getProcedureKey(Procedure.SS_STOP_CONFIG), curry(ss_stop_config_done, cloned_ext, ssGways, doneSSGways));
    }
