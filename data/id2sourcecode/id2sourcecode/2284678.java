    @Override
    public void doWork() throws OperatorException {
        String source;
        switch(getParameterAsInt(PARAMETER_SOURCE_TYPE)) {
            case SOURCE_TYPE_FILE:
                File file = getParameterAsFile(PARAMETER_FILENAME);
                source = file.getAbsolutePath();
                SimpleFileObject result = new SimpleFileObject(file);
                result.getAnnotations().setAnnotation(Annotations.KEY_SOURCE, source);
                fileOutputPort.deliver(result);
                break;
            case SOURCE_TYPE_URL:
                URL url;
                try {
                    url = new URL(getParameterAsString(PARAMETER_URL));
                    source = url.toString();
                    URLConnection connection = url.openConnection();
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    Tools.copyStreamSynchronously(connection.getInputStream(), buffer, true);
                    BufferedFileObject result1 = new BufferedFileObject(buffer.toByteArray());
                    result1.getAnnotations().setAnnotation(Annotations.KEY_SOURCE, source);
                    fileOutputPort.deliver(result1);
                } catch (MalformedURLException e) {
                    throw new UserError(this, e, "313", getParameterAsString(PARAMETER_URL));
                } catch (IOException e) {
                    throw new UserError(this, e, "316", getParameterAsString(PARAMETER_URL), e.getMessage());
                }
                break;
            case SOURCE_TYPE_REPOSITORY:
                RepositoryLocation location = getParameterAsRepositoryLocation(PARAMETER_REPOSITORY_LOCATION);
                source = location.getAbsoluteLocation();
                RepositoryBlobObject result2 = new RepositoryBlobObject(location);
                result2.getAnnotations().setAnnotation(Annotations.KEY_SOURCE, source);
                fileOutputPort.deliver(result2);
                break;
            default:
                throw new OperatorException("Illegal source type: " + getParameterAsString(PARAMETER_SOURCE_TYPE));
        }
    }
