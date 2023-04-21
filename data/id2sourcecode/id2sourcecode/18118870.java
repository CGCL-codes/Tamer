    private boolean generateChecksums() throws BuildException {
        boolean checksumMatches = true;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        byte[] buf = new byte[readBufferSize];
        try {
            for (Enumeration e = includeFileMap.keys(); e.hasMoreElements(); ) {
                messageDigest.reset();
                File src = (File) e.nextElement();
                if (!isCondition) {
                    log("Calculating " + algorithm + " checksum for " + src, Project.MSG_VERBOSE);
                }
                fis = new FileInputStream(src);
                DigestInputStream dis = new DigestInputStream(fis, messageDigest);
                while (dis.read(buf, 0, readBufferSize) != -1) {
                }
                dis.close();
                fis.close();
                fis = null;
                byte[] fileDigest = messageDigest.digest();
                if (totalproperty != null) {
                    allDigests.put(src, fileDigest);
                }
                String checksum = createDigestString(fileDigest);
                Object destination = includeFileMap.get(src);
                if (destination instanceof java.lang.String) {
                    String prop = (String) destination;
                    if (isCondition) {
                        checksumMatches = checksumMatches && checksum.equals(property);
                    } else {
                        getProject().setNewProperty(prop, checksum);
                    }
                } else if (destination instanceof java.io.File) {
                    if (isCondition) {
                        File existingFile = (File) destination;
                        if (existingFile.exists()) {
                            try {
                                String suppliedChecksum = readChecksum(existingFile);
                                checksumMatches = checksumMatches && checksum.equals(suppliedChecksum);
                            } catch (BuildException be) {
                                checksumMatches = false;
                            }
                        } else {
                            checksumMatches = false;
                        }
                    } else {
                        File dest = (File) destination;
                        fos = new FileOutputStream(dest);
                        fos.write(format.format(new Object[] { checksum, src.getName() }).getBytes());
                        fos.write(StringUtils.LINE_SEP.getBytes());
                        fos.close();
                        fos = null;
                    }
                }
            }
            if (totalproperty != null) {
                Set keys = allDigests.keySet();
                Object[] keyArray = keys.toArray();
                Arrays.sort(keyArray, new Comparator() {

                    public int compare(Object o1, Object o2) {
                        File f1 = (File) o1;
                        File f2 = (File) o2;
                        return f1 == null ? (f2 == null ? 0 : -1) : (f2 == null ? 1 : f1.getName().compareTo(f2.getName()));
                    }
                });
                messageDigest.reset();
                for (int i = 0; i < keyArray.length; i++) {
                    File src = (File) keyArray[i];
                    byte[] digest = (byte[]) allDigests.get(src);
                    messageDigest.update(digest);
                    String fileName = (String) relativeFilePaths.get(src);
                    messageDigest.update(fileName.getBytes());
                }
                String totalChecksum = createDigestString(messageDigest.digest());
                getProject().setNewProperty(totalproperty, totalChecksum);
            }
        } catch (Exception e) {
            throw new BuildException(e, getLocation());
        } finally {
            FileUtils.close(fis);
            FileUtils.close(fos);
        }
        return checksumMatches;
    }
