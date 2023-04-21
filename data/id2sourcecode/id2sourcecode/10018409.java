    public void storeClasses(ZipOutputStream zip) {
        for (Iterator i = getChilds(); i.hasNext(); ) {
            Identifier ident = (Identifier) i.next();
            if ((Main.stripping & Main.STRIP_UNREACH) != 0 && !ident.isReachable()) {
                if (GlobalOptions.verboseLevel > 4) GlobalOptions.err.println("Class/Package " + ident.getFullName() + " is not reachable");
                continue;
            }
            if (ident instanceof PackageIdentifier) ((PackageIdentifier) ident).storeClasses(zip); else {
                try {
                    String filename = ident.getFullAlias().replace('.', '/') + ".class";
                    zip.putNextEntry(new ZipEntry(filename));
                    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(zip));
                    ((ClassIdentifier) ident).storeClass(out);
                    out.flush();
                    zip.closeEntry();
                } catch (java.io.IOException ex) {
                    GlobalOptions.err.println("Can't write Class " + ident.getName());
                    ex.printStackTrace(GlobalOptions.err);
                }
            }
        }
    }
