    private gw_signed_signature stopConfig(gw_public_key pkey, gw_soundness_proof proof) {
        gw_signed_signature signed_config = new gw_signed_signature();
        signed_config.public_key = pkey;
        hasher.update(XdrUtils.serialize(proof.config.config));
        signed_config.datahash = hasher.digest();
        return signed_config;
    }
