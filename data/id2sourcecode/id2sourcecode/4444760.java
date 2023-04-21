            @Override
            public void run() {
                exportLabel.setForeground(Color.black);
                final File weighingPostFile = new File(OjtConstants.WEIGHING_DIRECTORY, FileNameComposer.composeWeighingPostFileName(competitionDatas.getCompetitionDescriptor(), competitionDatas.getWeighingPost(), competitionDatas.getCompetitionFile().getName().substring(competitionDatas.getCompetitionFile().getName().lastIndexOf('.'))));
                if (!weighingPostFile.getParentFile().exists()) {
                    weighingPostFile.getParentFile().mkdirs();
                }
                try {
                    FileUtils.copyFile(competitionDatas.getCompetitionFile(), weighingPostFile);
                    exportLabel.setText("Pes�e enregistr�e dans le fichier : " + weighingPostFile.getAbsolutePath());
                } catch (final Exception ex) {
                    logger.error("Erreur lors de la cr�ation du fichier de pes�e : " + weighingPostFile, ex);
                    exportLabel.setForeground(Color.red);
                    exportLabel.setText("Erreur lors de l'enregistrement de la pes�e.");
                }
                addExportLabel.setForeground(Color.black);
                addExportLabel.setVisible(false);
                final String addWeighingSaveDirPath = OJTConfiguration.getInstance().getProperty(OJTConfiguration.ADD_WEIGHING_SAVE_DIRECTORY_PATH);
                if ((addWeighingSaveDirPath != null) && !addWeighingSaveDirPath.trim().isEmpty()) {
                    addExportLabel.setVisible(true);
                    try {
                        final File addWeighingPostFile = new File(new File(addWeighingSaveDirPath), FileNameComposer.composeWeighingPostFileName(competitionDatas.getCompetitionDescriptor(), competitionDatas.getWeighingPost(), competitionDatas.getCompetitionFile().getName().substring(competitionDatas.getCompetitionFile().getName().lastIndexOf('.'))));
                        FileUtils.copyFile(competitionDatas.getCompetitionFile(), addWeighingPostFile);
                        addExportLabel.setText("Pes�e enregistr�e dans le r�pertoire suppl�mentaire : " + addWeighingPostFile.getAbsolutePath());
                    } catch (final Exception ex) {
                        logger.error("Erreur lors de la cr�ation du fichier de pes�e dans le r�pertoire suppl�mentaire de sauvegarde : " + addWeighingSaveDirPath, ex);
                        addExportLabel.setForeground(Color.red);
                        addExportLabel.setText("Erreur lors de l'enregistrement de la pes�e dans le r�pertoire suppl�mentaire.");
                    }
                }
                stepFinish();
            }
