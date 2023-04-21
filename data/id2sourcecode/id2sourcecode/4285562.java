    public void init(ConfigDataIF config) throws Exception {
        final String method_tag = tag + ".init";
        super.init(config);
        assert config.contains("timeout") : "Need to define timeout (in ms) in cfg file";
        TIMEOUT_MS = config_get_long(config, "timeout");
        assert config.contains("ss_set_size") : "Need to define ss_set_size in cfg";
        SS_SET_SIZE = config_get_int(config, "ss_set_size");
        assert config.contains("PkeyFilename") : "Need to define PkeyFilename in cfg file";
        assert config.contains("SkeyFilename") : "Need to define SkeyFilename in cfg file";
        logger.info(method_tag + ": Reading keys from disk...");
        String pkey_filename = config_get_string(config, "PkeyFilename");
        String skey_filename = config_get_string(config, "SkeyFilename");
        _pkey = readPublicKey(pkey_filename, _key_factory);
        _skey = readPrivateKey(skey_filename, _key_factory);
        if ((_pkey == null) || (_skey == null)) throw new Exception(method_tag + ": Failed to read key pair from disk: " + "pkey=" + pkey_filename + ", skey=" + skey_filename);
        _cr_pkey = new cr_public_key(_pkey.getEncoded());
        _sig_engine.initSign(_skey);
        logger.info(method_tag + ": Reading keys from disk...done.");
        int cnt = config_get_int(config, "gateway_count");
        if (cnt == -1) {
            gway = new NodeId(config_get_string(config, "gateway"));
            gateways.addLast(gway);
        } else {
            String regex = "0x[0-9a-fA-F]+";
            String sig_alg = "SHA1withRSA";
            for (int i = 0; i < cnt; ++i) {
                gway = new NodeId(config_get_string(config, "gateway_" + i));
                gateways.addLast(gway);
                String gway_guid_str = config_get_string(config, "gateway_guid_" + i);
                if (!gway_guid_str.matches(regex)) {
                    BUG("gway_guid_" + i + "=" + gway_guid_str + " must match " + regex);
                }
                BigInteger gway_guid = new BigInteger(gway_guid_str.substring(2), 16);
                String gway_pkey_filename = config_get_string(config, "gateway_pkey_" + i);
                String gway_skey_filename = config_get_string(config, "gateway_skey_" + i);
                PublicKey gway_pkey = readPublicKey(gway_pkey_filename, _key_factory);
                PrivateKey gway_skey = readPrivateKey(gway_skey_filename, _key_factory);
                if ((gway_pkey == null) || (gway_skey == null)) throw new Exception(method_tag + ": Failed to read gway private" + " key from disk: " + " pkey=" + gway_skey_filename + ", gway_skey=" + gway_skey_filename);
                cr_public_key gway_cr_pkey = new cr_public_key(gway_pkey.getEncoded());
                _md.update(gway_cr_pkey.value);
                BigInteger gway_guid2 = byteArrayToBigInteger(_md.digest());
                assert gway_guid.equals(gway_guid2) : "gway_guid=" + GuidTools.guid_to_string(gway_guid) + " != gway_guid2=" + GuidTools.guid_to_string(gway_guid2);
                Signature gway_sig_engine = null;
                if (ostore.security.NativeRSAAlgorithm.available) {
                    java.security.Security.addProvider(new ostore.security.NativeRSAProvider());
                    gway_sig_engine = Signature.getInstance("SHA-1/RSA/PKCS#1", "NativeRSA");
                } else {
                    gway_sig_engine = Signature.getInstance(sig_alg);
                }
                gway_sig_engine.initSign(gway_skey);
                _gwayKeys.put(gway_guid, new ValueTriple<cr_public_key, PrivateKey, Signature>(gway_cr_pkey, gway_skey, gway_sig_engine));
            }
        }
        Class[] rpc_msg_types = new Class[] { antiquity.rpc.api.RpcReply.class, antiquity.rpc.api.RpcRegisterResp.class };
        for (Class clazz : rpc_msg_types) {
            Filter filter = new Filter();
            if (!filter.requireType(clazz)) BUG(tag + ": could not require type " + clazz.getName());
            if (antiquity.rpc.api.RpcReply.class.isAssignableFrom(clazz)) {
                if (!filter.requireValue("inbound", new Boolean(true))) BUG(tag + ": could not require inbound = true for " + clazz.getName());
            }
            if (!filter.requireValue("appId", appId)) BUG(tag + ": could not require appId = " + appId + " for " + clazz.getName());
            if (DEBUG) logger.info(tag + ": subscribing to " + clazz);
            classifier.subscribe(filter, my_sink);
        }
        logger.info(method_tag + ": " + "init done");
        logger.info(method_tag + ": " + "Accepting requests...");
    }
