            @Override
            public void run(CommandLine commandLine) {
                try {
                    String fileName = commandLine.getOptionValue("file");
                    if (StringUtil.isEmpty(fileName)) {
                        error("file not specified");
                        help();
                        return;
                    }
                    File file = new File(fileName);
                    if (file.exists() && !commandLine.hasOption("overwrite")) {
                        error("File " + fileName + " exists already and '-overwrite' isn't specified.");
                        return;
                    }
                    System.out.println("About to download into file " + fileName);
                    KwantuModel model = getModel(commandLine);
                    if (model == null) {
                        return;
                    }
                    ModelExporter.export(file, model);
                    System.out.println("Downloaded model " + model.getName() + " into file " + fileName);
                } catch (IOException ex) {
                    error("Unable to download into file: " + ex.getMessage());
                }
            }
