    private void sendRequestedFiles(Protocol protocol) throws IOException, ProtocolException, DatabaseException {
        File envDir = feederManager.getEnvImpl().getEnvironmentHome();
        try {
            while (true) {
                FileReq fileReq = protocol.read(namedChannel.getChannel(), FileReq.class);
                final String fileName = fileReq.getFileName();
                File file = new File(envDir, fileName);
                if (!file.exists()) {
                    throw EnvironmentFailureException.unexpectedState("Log file not found: " + fileName);
                }
                final long length = file.length();
                final long lastModified = file.lastModified();
                byte digest[] = null;
                FileInfoResp resp = null;
                Protocol.FileInfoResp cachedResp = feederManager.statResponses.get(fileName);
                byte cachedDigest[] = ((cachedResp != null) && (cachedResp.getFileLength() == length) && (cachedResp.getLastModifiedTime() == lastModified)) ? cachedResp.getDigestSHA1() : null;
                if (fileReq instanceof FileInfoReq) {
                    if (cachedDigest != null) {
                        digest = cachedDigest;
                    } else if (((FileInfoReq) fileReq).getNeedSHA1()) {
                        digest = getSHA1Digest(file, length).digest();
                    } else {
                        digest = new byte[0];
                    }
                    resp = protocol.new FileInfoResp(fileName, length, lastModified, digest);
                } else {
                    protocol.write(protocol.new FileStart(fileName, length, lastModified), namedChannel);
                    digest = sendFileContents(file, length);
                    if ((cachedDigest != null) && !Arrays.equals(cachedDigest, digest)) {
                        throw EnvironmentFailureException.unexpectedState("Inconsistent cached and computed digests");
                    }
                    resp = protocol.new FileEnd(fileName, length, lastModified, digest);
                }
                if (digest.length > 0) {
                    feederManager.statResponses.put(fileName, resp);
                }
                protocol.write(resp, namedChannel);
            }
        } catch (ProtocolException pe) {
            if (pe.getUnexpectedMessage() instanceof Protocol.Done) {
                return;
            }
            throw pe;
        }
    }
