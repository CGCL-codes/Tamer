    @Override
    protected Object doExecute() throws Exception {
        ServiceReference ref = getBundleContext().getServiceReference(IdentityMediationUnitRegistry.class.getName());
        if (ref == null) {
            cmdPrinter.printMsg("Identity Mediation Unit Registry Service is unavailable. (no service reference)");
            return null;
        }
        try {
            IdentityMediationUnitRegistry svc = (IdentityMediationUnitRegistry) getBundleContext().getService(ref);
            if (svc == null) {
                cmdPrinter.printMsg("Identity Mediation Unit Registry  Service service is unavailable. (no service)");
                return null;
            }
            IdentityMediationUnit idau = svc.lookupUnit(idauId);
            if (idau == null) {
                throw new Exception("IdAU not found " + idauId);
            }
            if (verbose) cmdPrinter.printMsg("IdAU " + idau.getName());
            PsPChannel pspChannel = null;
            ProvisioningServiceProvider psp = null;
            for (Channel c : idau.getChannels()) {
                if (c instanceof PsPChannel) {
                    PsPChannel pc = (PsPChannel) c;
                    if (pc.getProvider() != null && pc.getProvider().getName().equals(pspId)) {
                        pspChannel = pc;
                        psp = pc.getProvider();
                        break;
                    }
                }
            }
            if (pspChannel == null || psp == null) {
                throw new Exception("PSP not found " + pspId);
            }
            if (verbose) cmdPrinter.printMsg("PSP " + psp.getName());
            if (verbose) cmdPrinter.printMsg("PSP Channel " + pspChannel.getName());
            doExecute(psp, pspChannel);
        } finally {
            getBundleContext().ungetService(ref);
        }
        return null;
    }
