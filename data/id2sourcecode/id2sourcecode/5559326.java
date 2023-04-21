    public void disseminate(Context context, DSpaceObject dso, PackageParameters params, OutputStream pkg) throws PackageValidationException, CrosswalkException, AuthorizeException, SQLException, IOException {
        if (dso.getType() == Constants.ITEM) {
            Item item = (Item) dso;
            long lmTime = item.getLastModified().getTime();
            String unauth = (params == null) ? null : params.getProperty("unauthorized");
            if (params != null && params.getProperty("manifestOnly") != null) {
                extraFiles = null;
                writeManifest(context, item, params, pkg);
            } else {
                extraFiles = new HashMap();
                ZipOutputStream zip = new ZipOutputStream(pkg);
                zip.setComment("METS archive created by DSpace METSDisseminationCrosswalk");
                ZipEntry me = new ZipEntry(MANIFEST_FILE);
                me.setTime(lmTime);
                zip.putNextEntry(me);
                writeManifest(context, item, params, zip);
                zip.closeEntry();
                Iterator fi = extraFiles.keySet().iterator();
                while (fi.hasNext()) {
                    String fname = (String) fi.next();
                    ZipEntry ze = new ZipEntry(fname);
                    ze.setTime(lmTime);
                    zip.putNextEntry(ze);
                    Utils.copy((InputStream) extraFiles.get(fname), zip);
                    zip.closeEntry();
                }
                Bundle bundles[] = item.getBundles();
                for (int i = 0; i < bundles.length; i++) {
                    if (!PackageUtils.isMetaInfoBundle(bundles[i])) {
                        if (!AuthorizeManager.authorizeActionBoolean(context, bundles[i], Constants.READ)) {
                            if (unauth != null && (unauth.equalsIgnoreCase("skip"))) {
                                log.warn("Skipping Bundle[\"" + bundles[i].getName() + "\"] because you are not authorized to read it.");
                                continue;
                            } else throw new AuthorizeException("Not authorized to read Bundle named \"" + bundles[i].getName() + "\"");
                        }
                        Bitstream[] bitstreams = bundles[i].getBitstreams();
                        for (int k = 0; k < bitstreams.length; k++) {
                            boolean auth = AuthorizeManager.authorizeActionBoolean(context, bitstreams[k], Constants.READ);
                            if (auth || (unauth != null && unauth.equalsIgnoreCase("zero"))) {
                                ZipEntry ze = new ZipEntry(makeBitstreamName(bitstreams[k]));
                                ze.setTime(lmTime);
                                ze.setSize(auth ? bitstreams[k].getSize() : 0);
                                zip.putNextEntry(ze);
                                if (auth) Utils.copy(bitstreams[k].retrieve(), zip); else log.warn("Adding zero-length file for Bitstream, SID=" + String.valueOf(bitstreams[k].getSequenceID()) + ", not authorized for READ.");
                                zip.closeEntry();
                            } else if (unauth != null && unauth.equalsIgnoreCase("skip")) {
                                log.warn("Skipping Bitstream, SID=" + String.valueOf(bitstreams[k].getSequenceID()) + ", not authorized for READ.");
                            } else {
                                throw new AuthorizeException("Not authorized to read Bitstream, SID=" + String.valueOf(bitstreams[k].getSequenceID()));
                            }
                        }
                    }
                }
                zip.close();
                extraFiles = null;
            }
        } else throw new PackageValidationException("Can only disseminate an Item now.");
    }
