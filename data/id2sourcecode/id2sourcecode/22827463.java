    @Override
    public AssemblyShader loadShader(URL url, ShaderType type) throws IOException {
        AssemblyShader shader = loadShader(url.openStream(), type);
        return (shader);
    }
