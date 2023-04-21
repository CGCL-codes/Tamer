    private Map<Permission, PermissionState> getPolicies(CodeSigner signer) {
        CodeSigner sig = null;
        for (CodeSigner s : policies.keySet()) {
            if (s == signer || s.equals(signer)) {
                sig = s;
            }
        }
        CombinationMap<Permission, PermissionState> perm = policies.get(sig);
        if (perm == null) {
            try {
                String name = Base64.encodeBytes(digester.digest((signer == null ? "null" : signer.toString()).getBytes()));
                perm = new CombinationMap<Permission, PermissionState>(NamingUtils.asName(this.getClass()).add(name), DynSecurityPolicy.ENCLOSURE_PERM);
                policies.put(signer, perm);
            } catch (InvalidNameException ine) {
                throw new RuntimeException(ine);
            }
        }
        return perm;
    }
