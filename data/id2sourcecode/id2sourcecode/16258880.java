    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("ZOOMIN")) {
            if (mouseClickStart >= 0 && mouseClickEnd >= 0 && mouseClickStart != mouseClickEnd) {
                if (mouseClickStart > mouseClickEnd) {
                    int t = mouseClickStart;
                    mouseClickStart = mouseClickEnd;
                    mouseClickEnd = t;
                }
                float sScale = (endDisplayIndex - startDisplayIndex) / (float) getWidth();
                endDisplayIndex = startDisplayIndex + (int) (mouseClickEnd * sScale);
                startDisplayIndex = startDisplayIndex + (int) (mouseClickStart * sScale);
                refresh();
            } else {
                int mid = (startDisplayIndex + endDisplayIndex) / 2;
                int dist = mid - startDisplayIndex;
                dist = (int) (dist * 0.9f);
                startDisplayIndex = mid - dist;
                endDisplayIndex = mid + dist;
                refresh();
            }
        } else if (ae.getActionCommand().equals("ZOOMOUT")) {
            int mid = (startDisplayIndex + endDisplayIndex) / 2;
            int dist = mid - startDisplayIndex;
            dist = (int) (dist * 1.8f);
            startDisplayIndex = mid - dist;
            endDisplayIndex = mid + dist;
            refresh();
        } else if (ae.getActionCommand().equals("CUT")) {
            if (mouseClickStart >= 0 && mouseClickEnd >= 0 && mouseClickStart != mouseClickEnd) {
                if (mouseClickStart > mouseClickEnd) {
                    int t = mouseClickStart;
                    mouseClickStart = mouseClickEnd;
                    mouseClickEnd = t;
                }
                float sScale = (endDisplayIndex - startDisplayIndex) / (float) getWidth();
                int endCut = startDisplayIndex + (int) (mouseClickEnd * sScale);
                int startCut = startDisplayIndex + (int) (mouseClickStart * sScale);
                if (startCut < startDisplayIndex) startCut = startDisplayIndex;
                if (endCut > endDisplayIndex) endCut = endDisplayIndex;
                int newLen = sample.length - (endCut - startCut);
                float[][] newSampleData = new float[sample.numChannels][newLen];
                for (int y = 0; y < sample.numChannels; y++) {
                    for (int x = 0; x < startCut; x++) {
                        newSampleData[y][x] = sample.sampleData[y][x];
                    }
                    for (int x = endCut; x < sample.length; x++) {
                        newSampleData[y][startCut + x - endCut] = sample.sampleData[y][x];
                    }
                }
                sample.sampleData = newSampleData;
                sample.length = newLen;
                gui.sampleUpdated(sample);
                refresh();
            }
        }
    }
