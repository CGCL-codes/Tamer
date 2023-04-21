    protected String writeSerializationPolicyFile(TreeLogger logger, GeneratorContext ctx, SerializableTypeOracle serializationSto, SerializableTypeOracle deserializationSto) throws UnableToCompleteException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(baos, SerializationPolicyLoader.SERIALIZATION_POLICY_FILE_ENCODING);
            TypeOracle oracle = ctx.getTypeOracle();
            PrintWriter pw = new PrintWriter(osw);
            JType[] serializableTypes = unionOfTypeArrays(serializationSto.getSerializableTypes(), deserializationSto.getSerializableTypes(), new JType[] { serviceIntf });
            for (int i = 0; i < serializableTypes.length; ++i) {
                JType type = serializableTypes[i];
                String binaryTypeName = TypeOracleMediator.computeBinaryClassName(type);
                pw.print(binaryTypeName);
                pw.print(", " + Boolean.toString(deserializationSto.isSerializable(type)));
                pw.print(", " + Boolean.toString(deserializationSto.maybeInstantiated(type)));
                pw.print(", " + Boolean.toString(serializationSto.isSerializable(type)));
                pw.print(", " + Boolean.toString(serializationSto.maybeInstantiated(type)));
                pw.print(", " + typeStrings.get(type));
                pw.print(", " + SerializationUtils.getSerializationSignature(oracle, type));
                pw.print('\n');
                if ((type instanceof JClassType) && ((JClassType) type).isEnhanced()) {
                    JField[] fields = ((JClassType) type).getFields();
                    JField[] rpcFields = new JField[fields.length];
                    int numRpcFields = 0;
                    for (JField f : fields) {
                        if (f.isTransient() || f.isStatic() || f.isFinal()) {
                            continue;
                        }
                        rpcFields[numRpcFields++] = f;
                    }
                    pw.print(SerializationPolicyLoader.CLIENT_FIELDS_KEYWORD);
                    pw.print(',');
                    pw.print(binaryTypeName);
                    for (int idx = 0; idx < numRpcFields; idx++) {
                        pw.print(',');
                        pw.print(rpcFields[idx].getName());
                    }
                    pw.print('\n');
                }
            }
            pw.close();
            byte[] serializationPolicyFileContents = baos.toByteArray();
            String serializationPolicyName = Util.computeStrongName(serializationPolicyFileContents);
            String serializationPolicyFileName = SerializationPolicyLoader.getSerializationPolicyFileName(serializationPolicyName);
            OutputStream os = ctx.tryCreateResource(logger, serializationPolicyFileName);
            if (os != null) {
                os.write(serializationPolicyFileContents);
                GeneratedResource resource = ctx.commitResource(logger, os);
                ctx.commitArtifact(logger, new RpcPolicyFileArtifact(serviceIntf.getQualifiedSourceName(), resource));
            } else {
                logger.log(TreeLogger.TRACE, "SerializationPolicy file for RemoteService '" + serviceIntf.getQualifiedSourceName() + "' already exists; no need to rewrite it.", null);
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
