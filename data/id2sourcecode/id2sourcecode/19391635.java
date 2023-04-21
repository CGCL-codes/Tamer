    public void preprocess(String inputDirectory, String outputDirectory, String extensionName, String codeLanguage) throws IOException, UserDefinedErrorException, EvaluationException {
        final File fInputDirectory = new File(inputDirectory);
        System.out.println(inputDirectory);
        final String[] files = fInputDirectory.list();
        for (String fileName : files) {
            final String fullCurrentFileName = inputDirectory + File.separator + fileName;
            final String fullCurrentOutputFileName = outputDirectory + File.separator + fileName;
            final File currentInputFile = new File(fullCurrentFileName);
            final File currentOutputFile = new File(fullCurrentOutputFileName);
            if (currentInputFile.isDirectory()) {
                currentOutputFile.mkdir();
                preprocess(fullCurrentFileName, fullCurrentOutputFileName, extensionName, codeLanguage);
            } else if (fullCurrentFileName.endsWith(extensionName)) {
                final String inputFileContent = FileUtils.readFileToString(currentInputFile);
                final String directiveStart = this.languageDirectiveStart.get(codeLanguage);
                final PreprocessorResult result = this.preprocessor.preprocess(fullCurrentFileName, directiveStart, inputFileContent, this.variables);
                final String outputFileContent = result.getPreprocessedCode();
                FileUtils.writeStringToFile(currentOutputFile, outputFileContent);
            } else {
                FileUtils.copyFile(currentInputFile, currentOutputFile);
            }
        }
    }
