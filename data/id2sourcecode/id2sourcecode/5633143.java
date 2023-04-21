    public void convert(InputStream in, OutputStream out, ConversionParameters params) throws ConverterException {
        Reader reader;
        Writer writer;
        Charset processInCharset;
        Charset processOutCharset;
        if ((getProcessInEncoding() == null && getProcessOutEncoding() == null) || (params.getSourceCharset().equalsIgnoreCase(getProcessInEncoding()) && params.getTargetCharset().equalsIgnoreCase(getProcessOutEncoding()))) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Converting raw input.");
            }
            remoteExec(in, out);
        } else {
            CharacterNormalizer _inputNormalizer = this.procInputCharNormalizer;
            CharacterNormalizer _outputNormalizer = this.procOutputCharNormalizer;
            if (getProcessInEncoding() != null && !getProcessInEncoding().equals(params.getSourceCharset())) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Setting input reader to charset [" + params.getSourceCharset() + "].");
                    LOGGER.debug("Setting process in to charset [" + getProcessInEncoding() + "].");
                }
                processInCharset = CharsetUtil.forName(getProcessInEncoding());
                reader = new InputStreamReader(in, CharsetUtil.forName(params.getSourceCharset()));
                _inputNormalizer = null;
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Setting input reader to charset [" + IDENTITY_CHARSET.displayName() + "].");
                    LOGGER.debug("Setting process in to charset [" + IDENTITY_CHARSET.displayName() + "].");
                }
                processInCharset = IDENTITY_CHARSET;
                reader = new InputStreamReader(in, IDENTITY_CHARSET);
                _inputNormalizer = null;
            }
            if (getProcessOutEncoding() != null && !getProcessOutEncoding().equals(params.getTargetCharset())) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Setting output writer to charset [" + params.getTargetCharset() + "].");
                    LOGGER.debug("Setting process out to charset [" + getProcessOutEncoding() + "].");
                }
                processOutCharset = CharsetUtil.forName(getProcessOutEncoding());
                writer = new OutputStreamWriter(out, CharsetUtil.forName(params.getTargetCharset()));
                _outputNormalizer = null;
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Setting output writer to charset [" + IDENTITY_CHARSET.displayName() + "].");
                    LOGGER.debug("Setting process out to charset [" + IDENTITY_CHARSET.displayName() + "].");
                }
                processOutCharset = IDENTITY_CHARSET;
                writer = new OutputStreamWriter(out, IDENTITY_CHARSET);
            }
            try {
                remoteExec(reader, writer, processInCharset, processOutCharset, _inputNormalizer, _outputNormalizer);
            } catch (IOException e) {
                throw new ConverterException(e);
            } catch (JSchException e) {
                throw new ConverterException(e);
            }
        }
    }
