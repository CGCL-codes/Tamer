        private BpmRecord(BPM bpm) {
            this.strId = bpm.getId();
            this.bolSelected = true;
            this.bolActive = true;
            this.xChannel = bpm.getChannel(STR_BPM_AMP_TBT_X);
            this.yChannel = bpm.getChannel(STR_BPM_AMP_TBT_Y);
        }
