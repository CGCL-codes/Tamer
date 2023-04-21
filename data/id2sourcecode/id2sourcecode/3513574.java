    public void mergeSameChan() {
        int[] sps = new int[this.lpartNum];
        for (int i = 0; i < this.lpartNum; i++) {
            sps[i] = lpartArr[i].getScope().getValueInt();
        }
        for (int i = 0; i < this.lpartNum; i++) {
            for (int j = i + 1; j < this.lpartNum; j++) {
                if (lpartArr[i].getPart().getChannel() == lpartArr[j].getPart().getChannel()) {
                    sps[j] = Math.max(sps[j], sps[i]);
                    sps[i] = sps[j];
                }
            }
        }
        int cnt = 0;
        for (int i = 0; i < this.lpartNum; i++) {
            if (lpartArr[i].getScope().getValue() < sps[i]) {
                Mod.repeatRT(lpartArr[i].getPart(), lpartArr[i].getScope().getValue(), sps[i]);
            }
            for (int j = i + 1; j < this.lpartNum; j++) {
                if (lpartArr[i].getPart().getChannel() == lpartArr[j].getPart().getChannel()) {
                    if (lpartArr[j].getScope().getValue() < sps[j]) {
                        Mod.repeatRT(lpartArr[j].getPart(), lpartArr[j].getScope().getValue(), sps[i + cnt]);
                    }
                    lpartArr[i].getPart().addPhraseList(lpartArr[j].getPart().getPhraseArray(), false);
                    Mod.quickSort(lpartArr[i].getPart());
                    this.remove(j);
                    cnt++;
                    j--;
                }
            }
        }
        updateIDChannels();
    }
