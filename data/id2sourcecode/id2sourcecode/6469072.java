    private boolean generateChecksums() {
        boolean checksumMatches = true;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        byte[] buf = new byte[readBufferSize];
        try {
            for (Enumeration e = includeFileMap.keys(); e.hasMoreElements(); ) {
                messageDigest.reset();
                File src = (File) e.nextElement();
                if (!isCondition) {
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
                    }
                } else if (destination instanceof java.io.File) {
                    if (isCondition) {
                        File existingFile = (File) destination;
                        if (existingFile.exists()) {
                            try {
                                String suppliedChecksum = readChecksum(existingFile);
                                checksumMatches = checksumMatches && checksum.equals(suppliedChecksum);
                            } catch (Exception be) {
                                checksumMatches = false;
                            }
                        } else {
                            checksumMatches = false;
                        }
                    } else {
                        File dest = (File) destination;
                        fos = new FileOutputStream(dest);
                        fos.write(format.format(new Object[] { checksum, src.getName() }).getBytes());
                        fos.write(System.getProperty("line.separator").getBytes());
                        fos.close();
                        fos = null;
                    }
                }
            }
            if (totalproperty != null) {
                Set keys = allDigests.keySet();
                Object[] keyArray = keys.toArray();
                Arrays.sort(keyArray);
                messageDigest.reset();
                for (int i = 0; i < keyArray.length; i++) {
                    File src = (File) keyArray[i];
                    byte[] digest = (byte[]) allDigests.get(src);
                    messageDigest.update(digest);
                    String fileName = (String) relativeFilePaths.get(src);
                    messageDigest.update(fileName.getBytes());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(fis);
            close(fos);
        }
        return checksumMatches;
    }
