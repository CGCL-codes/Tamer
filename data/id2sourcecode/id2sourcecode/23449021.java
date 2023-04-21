    public static <T1 extends XdrAble, T2 extends XdrAble, T3 extends XdrAble, T4 extends XdrAble> T4 createRepairSignature(T1 proof, T2 latest_config, T3 pkey, Class<T4> sig_clazz, PrivateKey skey, MessageDigest md, KeyFactory keyFactory, Signature sigEngine) {
        XdrAble cert = null;
        XdrAble config = null;
        XdrAble cert_cert = null;
        XdrAble config_config = null;
        XdrAble config_config_extent_key = null;
        XdrAble latest_config_config = null;
        XdrAble latest_config_config_extent_key = null;
        XdrAble[] latest_config_config_ss_set = null;
        try {
            Field field = proof.getClass().getField("cert");
            cert = (XdrAble) field.get(proof);
            field = proof.getClass().getField("config");
            config = (XdrAble) field.get(proof);
            assert cert != null && config != null : "RepairStage.createRepairSignature: proof has null fields " + XdrUtils.toString(proof);
            field = cert.getClass().getField("cert");
            cert_cert = (XdrAble) field.get(cert);
            field = config.getClass().getField("config");
            config_config = (XdrAble) field.get(config);
            assert cert_cert != null && config_config != null : "RepairStage.createRepairSignature: one of the following is null" + " cert.cert=" + XdrUtils.toString(cert_cert) + " or config.config=" + XdrUtils.toString(config_config);
            field = latest_config.getClass().getField("config");
            latest_config_config = (XdrAble) field.get(latest_config);
            assert latest_config_config != null : "RepairStage.createRepairSignature: latest_config has null fields " + XdrUtils.toString(latest_config);
            field = config_config.getClass().getField("extent_key");
            config_config_extent_key = (XdrAble) field.get(config_config);
            assert config_config_extent_key != null : "RepairStage.createRepairSignature: config.extent_key is null for config " + XdrUtils.toString(config_config);
            field = latest_config_config.getClass().getField("extent_key");
            latest_config_config_extent_key = (XdrAble) field.get(latest_config_config);
            field = latest_config_config.getClass().getField("ss_set");
            latest_config_config_ss_set = (XdrAble[]) field.get(latest_config_config);
            assert latest_config_config_extent_key != null && latest_config_config_ss_set != null : "RepairStage.createRepairSignature:" + " latest_config.{ss_set or extent_key} is null for latest_config " + XdrUtils.toString(latest_config_config);
            assert XdrUtils.equals(latest_config_config_extent_key, config_config_extent_key) : "RepairStage.createRepairSignature:" + " latest_config.extent_key=" + XdrUtils.toString(latest_config_config_extent_key) + " != proof.config.extent_key=" + XdrUtils.toString(config_config_extent_key);
            field = pkey.getClass().getField("value");
            byte[] pkey_value = (byte[]) field.get(pkey);
            assert pkey_value != null : "RepairStage.createRepairSignature:" + " pkey has null value field " + XdrUtils.toString(pkey);
            md.update(pkey_value);
            byte[] pkey_hash = md.digest();
            XdrAble pkey_guid = latest_config_config_extent_key.getClass().newInstance();
            field = pkey_guid.getClass().getField("value");
            field.set(pkey_guid, pkey_hash);
            int index = getSSIndex(pkey_guid, latest_config_config_ss_set, true);
            assert index >= 0 : "RepairStage.createRepairSignatue:" + "H(pkey)=" + XdrUtils.toString(pkey_guid) + " not contained in latest_config.ss_set=" + XdrUtils.toString(latest_config_config_ss_set) + ".  pkey=" + XdrUtils.toString(pkey);
        } catch (Exception e) {
            assert false : "Could not obtain public fields from proof=" + XdrUtils.toString(proof) + " or latest_config=" + XdrUtils.toString(latest_config) + ". " + e;
        }
        md.update(serialize(cert_cert));
        md.update(serialize(config_config));
        md.update(serialize(latest_config_config));
        byte[] sig_datahash = md.digest();
        byte[] sig_signature = null;
        try {
            sig_signature = createSignature(sig_datahash, skey, sigEngine);
        } catch (Exception e) {
            assert false : "RepairStage.createSignature: " + e;
        }
        T4 sig = null;
        try {
            sig = sig_clazz.newInstance();
            Field field = sig_clazz.getField("public_key");
            field.set(sig, pkey);
            field = sig_clazz.getField("datahash");
            field.set(sig, sig_datahash);
            field = sig_clazz.getField("signature");
            field.set(sig, sig_signature);
        } catch (Exception e) {
            assert false : "No value field in " + sig_clazz.getName() + ". " + e;
        }
        return sig;
    }
