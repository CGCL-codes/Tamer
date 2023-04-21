    @Override
    public GLSLShader loadShader(URL url, ShaderType type) throws IOException {
        GLSLShader shader = loadShader(url.openStream(), type);
        return (shader);
    }
