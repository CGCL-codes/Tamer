        @Override
        void visit(RecordedSound xmlRecSound, BaseParams parentParams) throws Exception {
            RecordedSoundProperties recSndProp = new RecordedSoundProperties();
            recSndProp.setRawSoundFile(xmlRecSound.getRawSoundFile());
            recSndProp.setStartOfs(xmlRecSound.getRawSoundFileStartOfsSecs());
            recSndProp.setDuration(xmlRecSound.getDurationSecs());
            recSndProp.setLeftChanSelected(getChanSelectorLeft(xmlRecSound.getChannelSelector()));
            recSndProp.setAmplitudeGraphExtentMult(xmlRecSound.getAmplitudeExtMult());
            IdAndName amplitudeGraphNameAndId = GraphDBDispatcher.getId(xmlRecSound.getAmplitudeGraph(), "Amplitude", false, m_importPrefix, m_importSuffix);
            recSndProp.setAmplitudeGraphId(amplitudeGraphNameAndId.getId());
            recSndProp.setAmplitudeGraph(amplitudeGraphNameAndId.getDbName());
            recSndProp.setIsKeepIntermediateFile(xmlRecSound.isKeepIntermediateFile());
            BaseParams params = new BaseParams(recSndProp);
            parentParams.add(params);
        }
