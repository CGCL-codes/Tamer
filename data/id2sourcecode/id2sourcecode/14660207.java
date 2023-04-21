    private void send_cr_create(cr_signed_certificate cert) {
        final String method_tag = tag + ".send_cr_create";
        logger.info(method_tag + ": " + "Calling cr_create procedure...");
        cr_create_ss_set_args create_args = new cr_create_ss_set_args();
        create_args.cert = cert;
        int f = 1;
        create_args.proposed_config = new cr_ss_set_config();
        create_args.proposed_config.type = create_args.cert.cert.type;
        create_args.proposed_config.redundancy_type = CR_REDUNDANCY_TYPE_REPL;
        create_args.proposed_config.ss_set_size = (byte) (3 * f + 1);
        create_args.proposed_config.inv_rate = create_args.proposed_config.ss_set_size;
        create_args.proposed_config.threshold = (byte) (2 * f + 1);
        _md.update(cert.cert.public_key.value);
        create_args.proposed_config.client_id = new cr_guid(_md.digest());
        create_args.proposed_config.extent_key = create_args.proposed_config.client_id;
        create_args.proposed_config.public_key = CR_NULL_PUBLIC_KEY;
        create_args.proposed_config.ss_set = new cr_id[0];
        dispatch(gway, create_args, getProcedureKey(Procedure.CR_CREATE_SS_SET), create_cr_done);
    }
