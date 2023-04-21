    private void copyResources(File directory, Book b) throws IOException {
        FileUtils.copyFileToDirectory(new File(templateDir, "latex/tubaina.sty"), directory);
        FileUtils.copyFileToDirectory(new File(templateDir, "latex/xcolor.sty"), directory);
        File[] images = new File(templateDir, "latex").listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.contains(".png") || name.contains(".bib");
            }
        });
        for (File image : images) {
            FileUtils.copyFileToDirectory(image, directory);
        }
        boolean resourceCopyFailed = false;
        File answerFile = new File(directory, "answer.tex");
        if (answerFile.exists()) {
            LOG.warn("Answer File already exists. Deleting it");
            answerFile.delete();
        }
        if (!noAnswer && hasAnswer(b.getChapters())) {
            PrintStream stream = new PrintStream(new FileOutputStream(answerFile), true, "UTF-8");
            stream.println("\\chapter{\\answerBooklet}");
            stream.close();
        }
        for (Chapter c : b.getChapters()) {
            ResourceManipulator manipulator = new LatexResourceManipulator(directory, answerFile, parser, noAnswer);
            for (Resource r : c.getResources()) {
                try {
                    r.copyTo(manipulator);
                } catch (TubainaException e) {
                    resourceCopyFailed = true;
                }
            }
        }
        if (resourceCopyFailed) throw new TubainaException("Couldn't copy some resources. See the Logger for further information");
    }
