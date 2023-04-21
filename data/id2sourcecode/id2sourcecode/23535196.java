        protected void setFctPvName(FCT fct) {
            this.fct = fct;
            this.FCTVoltPVName = fct.getChannel(FCT.VOLT_AVG_HANDLE).getId();
            this.FCTSwitchPVName = fct.getChannel(FCT.PHASE_SWITCH_HANDLE).getId();
            this.FCTSwitchRBPVName = fct.getChannel(FCT.SWITCH_RB_HANDLE).getId();
            this.FCTGainPVName = fct.getChannel(FCT.PHASE_GAIN_HANDLE).getId();
            this.FCTGainRBPVName = fct.getChannel(FCT.GAIN_RB_HANDLE).getId();
        }
