    protected void updateHardwareControls() {
        if (viewer.hardware instanceof dsPIC33F_COM_OpticalFlowHardwareInterface) {
            dsPIC33F_COM_OpticalFlowHardwareInterface hwi = (dsPIC33F_COM_OpticalFlowHardwareInterface) viewer.hardware;
            switch(hwi.getChannel()) {
                case MotionDataMDC2D.PHOTO:
                    channelRecep.setSelected(true);
                    break;
                case MotionDataMDC2D.LMC1:
                    channelLmc1.setSelected(true);
                    break;
                case MotionDataMDC2D.LMC2:
                    channelLmc2.setSelected(true);
                    break;
            }
            onChipADCSelector.setSelected(hwi.isOnChipADC());
        }
    }
