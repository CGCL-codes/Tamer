    protected void createModule(ExhibitionModuleReference reference) {
        Resource resource = getElementToEdit().eResource();
        ExhibitionModule module = ExhibitionFactory.eINSTANCE.createExhibitionModule();
        reference.setExhibitionModule(module);
        String filename = module.getExhibitionModuleId();
        IProject project = ProjectManager.getInstance().getCurrentProject();
        if (project == null) return;
        File moduleFile = new File(project.getLocation().toOSString() + File.separator + filename + "." + VSpaceExtensions.MODULE_EXTENSION);
        IFile moduleIFile = project.getFile(moduleFile.getName());
        if (!moduleFile.exists()) try {
            moduleFile.createNewFile();
        } catch (IOException e1) {
            ExceptionHandlingService.INSTANCE.handleException(e1);
            return;
        }
        try {
            moduleIFile.refreshLocal(1, null);
        } catch (CoreException e1) {
            e1.printStackTrace();
        }
        Resource anotherResource = resource.getResourceSet().createResource(URI.createPlatformResourceURI(moduleIFile.getFullPath().toOSString(), false));
        anotherResource.getContents().add(module);
        try {
            anotherResource.save(ExhibitionDiagramEditorUtil.getSaveOptions());
        } catch (IOException e) {
            ExceptionHandlingService.INSTANCE.handleException(e);
            return;
        }
        if (FunmodeManager.INSTANCE.getFunmode() == FunmodeManager.ENABLED) {
            Runnable runnable = new Runnable() {

                public void run() {
                    AudioInputStream inStrom = null;
                    AudioFormat format = null;
                    try {
                        String path = FileHandler.getAbsolutePath(ExhibitionDiagramEditorPlugin.ID, "/files/sounds/dolphy.wav");
                        File datei = new File(path);
                        inStrom = AudioSystem.getAudioInputStream(datei);
                        format = inStrom.getFormat();
                        if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                            AudioFormat neu = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 2 * format.getSampleSizeInBits(), format.getChannels(), 2 * format.getFrameSize(), format.getFrameRate(), true);
                            inStrom = AudioSystem.getAudioInputStream(neu, inStrom);
                            format = neu;
                        }
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    SourceDataLine line = null;
                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                    try {
                        line = (SourceDataLine) AudioSystem.getLine(info);
                        line.open(format);
                        line.start();
                        int num = 0;
                        byte[] audioPuffer = new byte[5000];
                        while (num != -1) {
                            try {
                                num = inStrom.read(audioPuffer, 0, audioPuffer.length);
                                if (num >= 0) line.write(audioPuffer, 0, num);
                            } catch (Exception ex) {
                                System.out.println(ex);
                            }
                        }
                        line.drain();
                        line.close();
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
            Notification noti = new Notification();
            noti.showNofication();
        }
    }
