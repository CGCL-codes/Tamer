            private void computeFile(File srcFile) {
                try {
                    final MessageDigest digest = MessageDigest.getInstance(params.getAlgorithm());
                    StreamingMultiDigester.compute(srcFile, digest);
                    pw.println(String.format("%s %s", BbxStringUtils.hexEncodeBytes(digest.digest()), srcFile));
                } catch (Exception e) {
                    logger.debug(e, "failed to compute checksum for %s", srcFile);
                    pw.println(String.format("-------------------------------- %s", srcFile));
                }
                pw.flush();
            }
