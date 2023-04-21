    public FixityResult calculateChecksum(DigitalObject digitalObject, List<Parameter> parameters) {
        FixityResult retResult = null;
        ServiceReport retReport = null;
        try {
            URI requestedAlgId = this.getDigestIdFromParameters(parameters);
            MessageDigest messDigest = MessageDigest.getInstance(JavaDigestUtils.getJavaAlgorithmName(requestedAlgId));
            InputStream inStream = digitalObject.getContent().getInputStream();
            if (this.addStreamBytesToDigest(messDigest, inStream, JavaDigest.DEFAULT_CHUNK_SIZE) < 1) {
                JavaDigest.log.severe(JavaDigest.NO_DATA_MESSAGE);
                retResult = this.createErrorResult(ServiceReport.Status.TOOL_ERROR, JavaDigest.NO_DATA_MESSAGE);
                return retResult;
            }
            retReport = new ServiceReport(ServiceReport.Type.INFO, ServiceReport.Status.SUCCESS, JavaDigest.SUCCESS_MESSAGE);
            retResult = new FixityResult(JavaDigestUtils.getDefaultAlgorithmId().toString(), messDigest.getProvider().getName(), messDigest.digest(), null, retReport);
        } catch (NoSuchAlgorithmException e) {
            retResult = this.createErrorResult(ServiceReport.Status.TOOL_ERROR, e.getMessage() + " for algorithm " + JavaDigestUtils.getDefaultAlgorithmId() + ".");
        } catch (IOException e) {
            retResult = this.createErrorResult(ServiceReport.Status.TOOL_ERROR, e.getMessage());
        } catch (URISyntaxException e) {
            retResult = this.createErrorResult(ServiceReport.Status.TOOL_ERROR, e.getMessage());
        }
        return retResult;
    }
