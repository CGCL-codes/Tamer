    private File copyCompetitionFileToSavedFile(final CompetitionDatas competitionDatas) throws IOException {
        final File savedFile = new File(OjtConstants.WEIGHING_DIRECTORY, FileNameComposer.composeSavedFileForAnotherSortName(competitionDatas.getCompetitionDescriptor(), competitionDatas.getCompetitionFile().getName().substring(competitionDatas.getCompetitionFile().getName().lastIndexOf('.'))));
        if (!savedFile.getParentFile().exists()) {
            savedFile.getParentFile().mkdirs();
        }
        FileUtils.copyFile(competitionDatas.getCompetitionFile(), savedFile);
        return savedFile;
    }
