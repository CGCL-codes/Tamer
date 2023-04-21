        @Override
        public void addSeries(MediaSeries series) {
            if (series instanceof DicomVideoSeries || series instanceof DicomEncapDocSeries) {
                if (AbstractProperties.OPERATING_SYSTEM.startsWith("linux")) {
                    FileExtractor extractor = (FileExtractor) series;
                    startAssociatedProgramFromLinux(extractor.getExtractFile());
                } else if (AbstractProperties.OPERATING_SYSTEM.startsWith("win")) {
                    FileExtractor extractor = (FileExtractor) series;
                    File file = extractor.getExtractFile();
                    startAssociatedProgramFromWinCMD(file.getAbsolutePath());
                } else if (Desktop.isDesktopSupported()) {
                    final Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.OPEN)) {
                        FileExtractor extractor = (FileExtractor) series;
                        startAssociatedProgramFromDesktop(desktop, extractor.getExtractFile());
                    }
                }
            }
        }
