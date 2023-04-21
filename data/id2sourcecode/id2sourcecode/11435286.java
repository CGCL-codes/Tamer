            @Override
            public boolean implies(final ProtectionDomain pd, final Permission p) {
                if (p instanceof SecureProperty.Permission) {
                    if (p.getName().equals("read") || p.getName().equals("write")) return false;
                    assert false : "\"read\" or \"write\" permission expected";
                }
                return true;
            }
