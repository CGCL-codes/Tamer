            private void computeSampleLevels() {
                sampleLevels = new float[calculateSampleLevelArrayLength()];
                float[] sampleData = sample.getFloatSampleBuffer().getChannel(0);
                for (int i = 0; i < sampleLevels.length; i++) {
                    float max = 0;
                    for (int j = sample.getStartOffset() + i * LEVEL_WINDOW_SIZE; j < ((i + 1) * LEVEL_WINDOW_SIZE); j++) {
                        if (j >= sampleData.length) break;
                        float current = Math.abs(sampleData[j]);
                        if (current > max) max = current;
                    }
                    sampleLevels[i] = max;
                }
            }
