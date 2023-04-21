    @Override
    protected void onRun(FutureManager<Void> fm, CommandContext ctx, String[] args) {
        if (args.length < 1) {
            ctx.printLine("Usage: identity <command> [arguments]");
            fm.completeFuture(null);
            return;
        }
        try {
            PeerService ps = ctx.obtainServiceReference(PeerService.class);
            if (args[0].equalsIgnoreCase("create")) {
                if (args.length != 5) {
                    ctx.printLine("Usage: identity create <domain name> <domain key-pair file> <local key-pair algorithm> <local key-pair cipher strength>");
                    fm.completeFuture(null);
                }
                KeyPairGenerator kpg = KeyPairGenerator.getInstance(args[3]);
                kpg.initialize(Integer.valueOf(args[4]));
                FileInputStream fis = new FileInputStream(args[2]);
                KeyPair domain_kp = Serialization.in(fis);
                fis.close();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                Serialization.out(args[1], os);
                Serialization.out(domain_kp, os);
                Serialization.out(kpg.generateKeyPair(), os);
                Serialization.out(new HashMap<MithrilKey, PublicKey>(), os);
                Serialization.out(new HashMap<String, MithrilKey>(), os);
                Serialization.out(0, os);
                os.close();
                ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
                ps.importIdentity(is);
                is.close();
            } else if (args[0].equalsIgnoreCase("import")) {
                if (args.length != 2) {
                    ctx.printLine("Usage: identity import <identity file>");
                    fm.completeFuture(null);
                    return;
                }
                FileInputStream fis = new FileInputStream(args[1]);
                ps.importIdentity(fis);
                fis.close();
            } else if (args[0].equalsIgnoreCase("export")) {
                if (args.length != 3) {
                    ctx.printLine("Usage: identity export <domain name> <identity file>");
                    fm.completeFuture(null);
                    return;
                }
                FileOutputStream fos = new FileOutputStream(args[2]);
                ps.exportIdentity(args[1], fos);
                fos.close();
            } else if (args[0].equalsIgnoreCase("delete")) {
                if (args.length != 2) {
                    ctx.printLine("Usage: identity delete <domain name>");
                    fm.completeFuture(null);
                    return;
                }
                ps.deleteIdentity(args[1]);
            } else {
                ctx.printLine("Identity management subcommand \"" + args[0] + "\" not recognized.");
            }
        } catch (Exception e) {
            ctx.printLine(e.toString());
        }
        fm.completeFuture(null);
    }
