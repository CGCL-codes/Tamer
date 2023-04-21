    public void renderFrame(String frameName, ProgressDisplay progressDisplay) {
        Settings settings = Settings.getInstance();
        ArrayList models = new ArrayList();
        ArrayList lights = new ArrayList();
        for (Iterator it = Animator.getInstance().getObjectList().iterator(); it.hasNext(); ) {
            Object o = it.next();
            if (o instanceof AnimModel) {
                models.add(o);
            }
            if (o instanceof AnimLight) {
                lights.add(o);
            }
        }
        switch(settings.export.rendererToUse) {
            case INYO:
                {
                    progressDisplay.clearText();
                    progressDisplay.addText("Working directory is: " + settings.export.workingDirectory + "\n");
                    progressDisplay.addText("Invoking Inyo...");
                    TextureParser.setTexturePath(settings.export.inyo.textureDirectory.getPath());
                    InyoRenderer3 renderer = new InyoRenderer3(models, Animator.getInstance().getActiveCamera(), lights);
                    Image image = renderer.render(new RtInterface());
                    if (image != null) {
                        File imageFile = new File(settings.export.workingDirectory, frameName + ".png");
                        try {
                            ImageIO.write((BufferedImage) image, "png", imageFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        progressDisplay.loadImage(imageFile);
                    }
                }
                break;
            case RENDERMAN:
                {
                    RibRenderer4 renderer = new RibRenderer4();
                    File ribFile = new File(settings.export.workingDirectory, frameName + ".rib");
                    progressDisplay.clearText();
                    progressDisplay.addText("Working directory is: " + settings.export.workingDirectory + "\n");
                    progressDisplay.addText("Generating geometry...");
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(ribFile));
                        renderer.writeToFile(models, Animator.getInstance().getActiveCamera(), lights, writer, frameName + ".tif");
                        writer.close();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    String[] ribCmd = { settings.export.renderman.executable.getPath(), frameName + ".rib" };
                    String[] ribEnv = settings.export.renderman.environmentVariables.split(";");
                    File imageFile = new File(settings.export.workingDirectory, frameName + ".tif");
                    if (imageFile.exists()) imageFile.delete();
                    StringBuffer sb = new StringBuffer();
                    sb.append("Starting RenderMAN renderer using:\n");
                    for (int i = 0; i < ribCmd.length; sb.append(ribCmd[i++]).append(" ")) ;
                    sb.append("\n");
                    if (ribEnv != null) {
                        sb.append("Environment variables:\n");
                        for (int i = 0; i < ribEnv.length; sb.append(ribEnv[i++]).append("\n")) ;
                    }
                    sb.append("\n");
                    sb.append("--------------------------------------------------------------------------------\n");
                    progressDisplay.addText(sb.toString());
                    try {
                        Process rib = Runtime.getRuntime().exec(ribCmd, ribEnv, settings.export.workingDirectory);
                        new ProcessMonitor(rib, progressDisplay);
                        rib.waitFor();
                        if (settings.export.deletePerFrameFilesAfterRendering) ribFile.delete();
                        progressDisplay.loadImage(imageFile);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                break;
            case POVRAY:
                {
                    progressDisplay.clearText();
                    progressDisplay.addText("Working directory is: " + settings.export.workingDirectory + "\n");
                    progressDisplay.addText("Generating geometry...");
                    File povrayFile = new File(settings.export.workingDirectory, frameName + ".pov");
                    try {
                        PovrayRenderer3 renderer = new PovrayRenderer3();
                        BufferedWriter writer = new BufferedWriter(new FileWriter(povrayFile));
                        renderer.writeFrame(models, camera1, lights, re.getRenderString("povray", ""), writer);
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ArrayList listCmd = new ArrayList();
                    listCmd.add(settings.export.povray.executable);
                    if (settings.export.povray.version == PovraySettings.Version.UNIX) {
                        listCmd.add("+I" + frameName + ".pov");
                    } else {
                        listCmd.add("/RENDER");
                        listCmd.add(frameName + ".pov");
                        listCmd.add("/EXIT");
                    }
                    listCmd.add("+O" + frameName + ".png");
                    listCmd.add("+W" + settings.export.imageWidth);
                    listCmd.add("+H" + settings.export.imageHeight);
                    listCmd.add("-D");
                    listCmd.add("-P");
                    listCmd.add("+FN8");
                    switch(settings.export.povray.antialiasingMethod) {
                        case OFF:
                            listCmd.add("-A");
                            break;
                        case METHOD_1:
                            listCmd.add("+A" + settings.export.povray.antialiasingThreshold);
                            listCmd.add("+AM 1");
                            listCmd.add("+R" + settings.export.povray.antialiasingLevel);
                            break;
                        case METHOD_2:
                            listCmd.add("+A" + settings.export.povray.antialiasingThreshold);
                            listCmd.add("+AM 2");
                            listCmd.add("+R" + settings.export.povray.antialiasingLevel);
                            break;
                    }
                    if (settings.export.povray.antialiasingJitter != 0) {
                        listCmd.add("+J" + settings.export.povray.antialiasingJitter);
                    } else {
                        listCmd.add("-J");
                    }
                    String[] povCmd = (String[]) listCmd.toArray(new String[0]);
                    String[] povEnv = settings.export.povray.environmentVariables.split(";");
                    StringBuffer sb = new StringBuffer();
                    sb.append("Starting POV-Ray renderer using:\n");
                    for (int i = 0; i < povCmd.length; sb.append(povCmd[i++]).append(" ")) ;
                    sb.append("\n");
                    if (povEnv != null) {
                        sb.append("Environment variables:\n");
                        for (int i = 0; i < povEnv.length; sb.append(povEnv[i++]).append("\n")) ;
                    }
                    sb.append("\n");
                    sb.append("--------------------------------------------------------------------------------\n");
                    progressDisplay.addText(sb.toString());
                    File imageFile = new File(settings.export.workingDirectory, frameName + ".png");
                    if (imageFile.exists()) imageFile.delete();
                    try {
                        Process pov = Runtime.getRuntime().exec(povCmd, povEnv, settings.export.workingDirectory);
                        progressDisplay.setRendererProcess(pov);
                        new ProcessMonitor(pov, progressDisplay);
                        pov.waitFor();
                        if (settings.export.deletePerFrameFilesAfterRendering) povrayFile.delete();
                        progressDisplay.loadImage(imageFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }
