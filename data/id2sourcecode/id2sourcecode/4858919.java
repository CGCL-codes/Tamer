    private ShaderProgram loadShaderProgram() {
        ShaderProgram sp = null;
        String vertexProgram = null;
        String fragmentProgram = null;
        Shader[] shaders = new Shader[2];
        try {
            ClassLoader cl = this.getClass().getClassLoader();
            URL url = cl.getResource("resources/Shaders/background.vert");
            System.out.println("url " + url);
            InputStream inputSteam = cl.getResourceAsStream("resources/Shaders/background.vert");
            Reader reader = null;
            if (inputSteam != null) {
                reader = new InputStreamReader(inputSteam);
            } else {
                File file = new File("lib");
                URL url2 = new URL("jar:file:" + file.getAbsolutePath() + "/resources.jar!/resources/Shaders/background.vert");
                InputStream inputSteam2 = url2.openStream();
                reader = new InputStreamReader(inputSteam2);
            }
            char[] buffer = new char[10000];
            int len = reader.read(buffer);
            vertexProgram = new String(buffer);
            vertexProgram = vertexProgram.substring(0, len);
        } catch (Exception e) {
            System.err.println("could'nt load background.vert");
            e.printStackTrace();
        }
        try {
            ClassLoader cl = this.getClass().getClassLoader();
            URL url = cl.getResource("resources/Shaders/background.frag");
            System.out.println("url " + url);
            InputStream inputSteam = cl.getResourceAsStream("resources/Shaders/background.frag");
            Reader reader = null;
            if (inputSteam != null) {
                reader = new InputStreamReader(inputSteam);
            } else {
                File file = new File("lib");
                URL url2 = new URL("jar:file:" + file.getAbsolutePath() + "/resources.jar/resources/Shaders/background.frag");
                InputStream inputSteam2 = url2.openStream();
                reader = new InputStreamReader(inputSteam2);
            }
            char[] buffer = new char[10000];
            int len = reader.read(buffer);
            fragmentProgram = new String(buffer);
            fragmentProgram = fragmentProgram.substring(0, len);
        } catch (Exception e) {
            System.err.println("could'nt load background.frag");
            e.printStackTrace();
        }
        if (vertexProgram != null && fragmentProgram != null) {
            shaders[0] = new SourceCodeShader(Shader.SHADING_LANGUAGE_GLSL, Shader.SHADER_TYPE_VERTEX, vertexProgram);
            shaders[1] = new SourceCodeShader(Shader.SHADING_LANGUAGE_GLSL, Shader.SHADER_TYPE_FRAGMENT, fragmentProgram);
            sp = new GLSLShaderProgram();
            sp.setShaders(shaders);
        }
        return sp;
    }
