    public final void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        try {
            int bufferOperations = l1 / convertBufferLength + 1;
            LProgressViewer.getInstance().entrySubProgress(0.7);
            for (int i = 0; i < bufferOperations; i++) {
                if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / bufferOperations)) return;
                for (int j = 0; j < convertBufferLength; j++) {
                    int jj = o1 + i * convertBufferLength + j;
                    if (jj < o1 + l1) {
                        re.set(j, s1.get(jj));
                    } else {
                        re.set(j, 0);
                    }
                    im.set(j, 0);
                }
                operateTWindow();
                AOToolkit.complexFft(re, im);
                addToSpectrum();
            }
            reduceSpectrum(bufferOperations);
            LProgressViewer.getInstance().exitSubProgress();
        } catch (ArrayIndexOutOfBoundsException oob) {
            Debug.printStackTrace(5, oob);
        }
    }
