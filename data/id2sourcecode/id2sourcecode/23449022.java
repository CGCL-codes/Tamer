    public static <T1 extends XdrAble, T2 extends XdrAble, T3 extends XdrAble> boolean verifyRepairSignature(Logger logger, T1 sig, T2 proof, T3 latest_config, MessageDigest md, KeyFactory keyFactory, Signature verifyEngine) {
        XdrAble cert = null;
        XdrAble config = null;
        XdrAble cert_cert = null;
        XdrAble config_config = null;
        XdrAble config_config_extent_key = null;
        XdrAble latest_config_config = null;
        XdrAble latest_config_config_extent_key = null;
        XdrAble[] latest_config_config_ss_set = null;
        byte[] sig_datahash = null;
        byte[] sig_signature = null;
        XdrAble sig_pk = null;
        byte[] sig_pk_value = null;
        try {
            Field field = proof.getClass().getField("cert");
            cert = (XdrAble) field.get(proof);
            field = proof.getClass().getField("config");
            config = (XdrAble) field.get(proof);
            if (cert == null || config == null) {
                if (logger.isInfoEnabled()) logger.info("RepairStage.verifyRepairSignature:" + " proof has null fields " + XdrUtils.toString(proof));
                return false;
            }
            field = cert.getClass().getField("cert");
            cert_cert = (XdrAble) field.get(cert);
            field = config.getClass().getField("config");
            config_config = (XdrAble) field.get(config);
            if (cert_cert == null || config_config == null) {
                if (logger.isInfoEnabled()) logger.info("RepairStage.verifyRepairSignature:" + " one of the following is null" + " cert.cert=" + XdrUtils.toString(cert_cert) + " or config.config=" + XdrUtils.toString(config_config));
                return false;
            }
            field = latest_config.getClass().getField("config");
            latest_config_config = (XdrAble) field.get(latest_config);
            if (latest_config_config == null) {
                if (logger.isInfoEnabled()) logger.info("RepairStage.verifyRepairSignature:" + " latest_config has null fields " + XdrUtils.toString(latest_config));
                return false;
            }
            field = config_config.getClass().getField("extent_key");
            config_config_extent_key = (XdrAble) field.get(config_config);
            if (config_config_extent_key == null) {
                if (logger.isInfoEnabled()) logger.info("RepairStage.verifyRepairSignature:" + " config.extent_key is null for config " + XdrUtils.toString(config_config));
                return false;
            }
            field = latest_config_config.getClass().getField("extent_key");
            latest_config_config_extent_key = (XdrAble) field.get(latest_config_config);
            field = latest_config_config.getClass().getField("ss_set");
            latest_config_config_ss_set = (XdrAble[]) field.get(latest_config_config);
            if (latest_config_config_extent_key == null || latest_config_config_ss_set == null) {
                if (logger.isInfoEnabled()) logger.info("RepairStage.verifyRepairSignature:" + " latest_config.{ss_set or extent_key} is null for" + " latest_config " + XdrUtils.toString(latest_config));
                return false;
            }
            if (!XdrUtils.equals(latest_config_config_extent_key, config_config_extent_key)) {
                if (logger.isInfoEnabled()) logger.info("RepairStage.verifyRepairSignature:" + " latest_config.extent_key=" + XdrUtils.toString(latest_config_config_extent_key) + " != proof.config.extent_key=" + XdrUtils.toString(config_config_extent_key));
                return false;
            }
            field = sig.getClass().getField("datahash");
            sig_datahash = (byte[]) field.get(sig);
            field = sig.getClass().getField("signature");
            sig_signature = (byte[]) field.get(sig);
            field = sig.getClass().getField("public_key");
            sig_pk = (XdrAble) field.get(sig);
            if (sig_datahash == null || sig_signature == null || sig_pk == null) {
                if (logger.isInfoEnabled()) logger.info("RepairStage.verifyRepairSignature:" + " sig object has null fields " + XdrUtils.toString(sig));
                return false;
            }
            field = sig_pk.getClass().getField("value");
            sig_pk_value = (byte[]) field.get(sig_pk);
            md.update(sig_pk_value);
            byte[] sig_pk_hash = md.digest();
            XdrAble sig_pk_guid = latest_config_config_extent_key.getClass().newInstance();
            field = sig_pk_guid.getClass().getField("value");
            field.set(sig_pk_guid, sig_pk_hash);
            int index = getSSIndex(sig_pk_guid, latest_config_config_ss_set, true);
            if (index < 0) {
                if (logger.isInfoEnabled()) logger.info("RepairStage.createRepairSignatue:" + "H(sig_pk)=" + XdrUtils.toString(sig_pk_guid) + " not contained in latest_config.ss_set=" + XdrUtils.toString(latest_config_config_ss_set) + ".  sig_pk=" + XdrUtils.toString(sig_pk));
                return false;
            }
        } catch (Exception e) {
            if (logger.isInfoEnabled()) logger.info("RepairStage.verifyRepairSignature" + "Could not obtain public fields from proof=" + XdrUtils.toString(proof) + " or latest_config=" + XdrUtils.toString(latest_config) + ". " + e);
            return false;
        }
        md.update(serialize(cert_cert));
        md.update(serialize(config_config));
        md.update(serialize(latest_config_config));
        byte[] datahash = md.digest();
        if (!ByteUtils.equals(sig_datahash, datahash)) {
            if (logger.isInfoEnabled()) logger.info("RepairStage.verifyRepairSignature: datahash in sig=" + XdrUtils.toString(sig) + " does not match datahash=" + ByteUtils.print_bytes(datahash, 0, num_bytes_to_print));
            return false;
        }
        boolean rv = false;
        try {
            rv = verifySignature(sig_datahash, sig_signature, sig_pk, keyFactory, verifyEngine);
        } catch (Exception e) {
        }
        return rv;
    }
