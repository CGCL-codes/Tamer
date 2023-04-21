    private void saveReadme(File file, int numberOfLinks, int sampleSize, int numberOfPositives, int numberOfNegatives) throws IOException {
        int numberOfUnsures = sampleSize - numberOfPositives - numberOfNegatives;
        String readme = FileUtils.readFileToString(readmeTemplateFile);
        readme = readme.replace("##date##", new Date().toString());
        readme = readme.replace("##number_of_links##", String.valueOf(numberOfLinks));
        readme = readme.replace("##sample_size##", String.valueOf(sampleSize));
        readme = readme.replace("##positive##", String.valueOf(numberOfPositives));
        readme = readme.replace("##negative##", String.valueOf(numberOfNegatives));
        readme = readme.replace("##unsure##", String.valueOf(numberOfUnsures));
        if (numberOfUnsures == 0) {
            readme = readme.replace("##precision##", String.valueOf((double) numberOfPositives / (numberOfPositives + numberOfNegatives)));
        } else {
            double precisionLowestEstimate = (double) numberOfPositives / (sampleSize);
            double precisionHighestEstimate = (double) (numberOfPositives + numberOfUnsures) / (sampleSize);
            readme = readme.replace("##precision##", precisionLowestEstimate + "-" + precisionHighestEstimate + " (depending on the unsure links).");
        }
        FileUtils.writeStringToFile(file, readme);
    }
