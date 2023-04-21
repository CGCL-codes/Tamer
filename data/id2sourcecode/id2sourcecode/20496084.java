    private String writeSerializationPolicyFile(TreeLogger logger, GeneratorContext ctx, SerializableTypeOracle sto) throws UnableToCompleteException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(baos, SerializationPolicyLoader.SERIALIZATION_POLICY_FILE_ENCODING);
            PrintWriter pw = new PrintWriter(osw);
            JType[] serializableTypes = sto.getSerializableTypes();
            for (int i = 0; i < serializableTypes.length; ++i) {
                JType serializableType = serializableTypes[i];
                String binaryTypeName = sto.getSerializedTypeName(serializableType);
                boolean maybeInstantiated = sto.maybeInstantiated(serializableType);
                pw.print(binaryTypeName + ", " + Boolean.toString(maybeInstantiated) + '\n');
            }
            pw.close();
            byte[] serializationPolicyFileContents = baos.toByteArray();
            String serializationPolicyName = Util.computeStrongName(serializationPolicyFileContents);
            String serializationPolicyFileName = SerializationPolicyLoader.getSerializationPolicyFileName(serializationPolicyName);
            OutputStream os = ctx.tryCreateResource(logger, serializationPolicyFileName);
            if (os != null) {
                os.write(serializationPolicyFileContents);
                ctx.commitResource(logger, os);
            } else {
                logger.log(TreeLogger.TRACE, "SerializationPolicy file for RemoteService '" + m_serviceIntf.getQualifiedSourceName() + "' already exists; no need to rewrite it.", null);
            }
            return serializationPolicyName;
        } catch (UnsupportedEncodingException e) {
            logger.log(TreeLogger.ERROR, SerializationPolicyLoader.SERIALIZATION_POLICY_FILE_ENCODING + " is not supported", e);
            throw new UnableToCompleteException();
        } catch (IOException e) {
            logger.log(TreeLogger.ERROR, null, e);
            throw new UnableToCompleteException();
        }
    }
