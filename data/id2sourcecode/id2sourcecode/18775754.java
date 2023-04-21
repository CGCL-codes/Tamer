    private void simpleGenerateAndCopyShader(List<Q3MapProject> inputMapProjects, String path) {
        AutoShader autoShader = new AutoShader();
        for (Q3MapProject project : inputMapProjects) {
            List<ShaderInfo> shaderInfoList = project.getShaderInfoList();
            Set<String> fileNameSet = new HashSet<String>();
            if (shaderInfoList != null) {
                for (ShaderInfo info : shaderInfoList) {
                    String shaderFileName = info.retrieveShaderFileName();
                    if (shaderFileName != null) {
                        if (info instanceof AutoShaderInfo) {
                            AutoShaderInfo autoShaderInfo = (AutoShaderInfo) info;
                            autoShader.autoShade(autoShaderInfo, path + IMixdownHandler.SCRIPTS_FOLDER + shaderFileName);
                        } else {
                            if (fileNameSet.contains(shaderFileName)) {
                                String error = FileUtils.copyFile(info.getInputShaderPath(), path + IMixdownHandler.SCRIPTS_FOLDER + shaderFileName);
                                if (StringUtils.notNullOrEmpty(error)) {
                                    System.out.println(error);
                                }
                            } else {
                                System.out.println("Warning: Shader file " + shaderFileName + " had duplicate name and was overwritten.");
                            }
                        }
                    } else {
                        System.out.println("File not found " + info.getInputShaderPath());
                    }
                }
            }
        }
    }
