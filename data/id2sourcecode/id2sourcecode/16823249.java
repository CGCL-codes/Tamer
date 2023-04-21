    public void generate(File input, File output, File rootInput) throws IOException {
        FileDetailGenerator generator = FileDetailGeneratorFactory.getInstance().getGenerator(input);
        String fileContent = generator.getHTML(input, rootInput);
        generatePage(rootInput, input, generator.getOutputFile(output), fileContent);
        if (input.isDirectory()) {
            output.mkdirs();
        } else {
            FileUtils.copyFile(input, output);
        }
    }
