    public boolean computeIntermediateFile(Number startTimeOffset, IComputeSound computeSound) throws Exception {
        String rawSoundFileName = m_properties.getRawSoundFile();
        if (Utils.isBlank(rawSoundFileName)) return false;
        File rawSoundFile = new File(rawSoundFileName);
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(rawSoundFile);
        } catch (FileNotFoundException e) {
            Dialogs.showNoWayDialog("Unable to find raw sound file");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        FileChannel in = fin.getChannel();
        try {
            int chunkSize = readRiffChunk(in);
            FormatChunkInfo info = readFormatChunk(in);
            int dataChunkSize = readDataChunkHeader(in);
            if (dataChunkSize == 0) {
                return false;
            }
            int numSamples = dataChunkSize / info.m_blockAlign;
            int padByte = 0;
            if (dataChunkSize % 2 != 0) {
                padByte = 1;
            }
            int dataChunkSamplesSize = dataChunkSize + padByte;
            int configBufSize = Preference.getSoundFileBufferSize();
            int bufSize = configBufSize == 0 ? dataChunkSamplesSize : Math.min(configBufSize, dataChunkSamplesSize);
            int targetSampleRate = Preference.getSampleRate();
            double targetSampleInc = 1.0d / targetSampleRate;
            int targetStartOfs = (int) (startTimeOffset.doubleValue() * targetSampleRate);
            int sourceSampleRate = info.m_sampleRate;
            int samplesToSkip = (int) (m_properties.getStartOfs() * sourceSampleRate);
            int sourceStartOfs = samplesToSkip * info.m_blockAlign;
            boolean equalSampleRate = sourceSampleRate == targetSampleRate;
            boolean useLeftChan = m_properties.getLeftChanSelected();
            int numChannels = info.m_numChannels;
            SampleInfo sampleInfo = null;
            int samplesToProcess = numSamples - samplesToSkip;
            double duration = m_peer.getDuration().doubleValue();
            int maxSamples = (int) (duration * sourceSampleRate);
            if (samplesToProcess > maxSamples) {
                samplesToProcess = maxSamples;
            }
            if (samplesToProcess <= 0) {
                return false;
            }
            double curTargetTime = targetSampleInc;
            double prevSourceTime = 0;
            double prevSourceSampleVal = 0;
            double multExtent = m_properties.getAmplitudeGraphExtentMult();
            int amplitudeGraphId = m_properties.getAmplitudeGraphId();
            IGraphEvaluator graphEval = null;
            if (amplitudeGraphId > 0) {
                loadAmplitudeGraph(amplitudeGraphId);
                graphEval = GraphEvaluatorFactory.newInstance(m_amplitudeGraphParams, duration, multExtent, null);
            }
            double graphEvalRes = 1.0d;
            int samplesInCalculationInterval = (int) (m_calculationIntervalSecs * sourceSampleRate);
            int sampleCountDown = 1;
            double[] sampleArr = computeSound.getSampleArr();
            int targetNdx = 0;
            SampleIter sampleIter = new SampleIter(bufSize, info.m_bitsPerSample, numChannels, useLeftChan, sourceSampleRate, samplesToProcess, in, info.m_blockAlign, sourceStartOfs);
            for (; sampleIter.hasNext(); ) {
                sampleInfo = sampleIter.next();
                double sampleVal = sampleInfo.m_sampleVal;
                if (graphEval == null) {
                    sampleVal *= multExtent;
                } else {
                    sampleCountDown--;
                    if (sampleCountDown == 0) {
                        sampleCountDown = samplesInCalculationInterval;
                        graphEvalRes = graphEval.evaluate(sampleInfo.m_sampleTime).doubleValue();
                    }
                    sampleVal *= graphEvalRes;
                }
                if (equalSampleRate) {
                    sampleArr[targetStartOfs + targetNdx] += sampleVal;
                    targetNdx++;
                } else {
                    double curSourceTime = sampleInfo.m_sampleTime;
                    double curSourceSampleVal = sampleVal;
                    while (curSourceTime >= curTargetTime) {
                        double targetSampleVal;
                        if (curSourceTime == curTargetTime) {
                            targetSampleVal = curSourceSampleVal;
                        } else {
                            targetSampleVal = prevSourceSampleVal + ((curSourceSampleVal - prevSourceSampleVal) * (curTargetTime - prevSourceTime)) / (curSourceTime - prevSourceTime);
                        }
                        sampleArr[targetStartOfs + targetNdx] += targetSampleVal;
                        curTargetTime += targetSampleInc;
                        targetNdx++;
                    }
                    prevSourceTime = curSourceTime;
                    prevSourceSampleVal = curSourceSampleVal;
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        } finally {
            in.close();
            fin.close();
        }
        return true;
    }
