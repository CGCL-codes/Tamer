    private void setIPD(boolean bHasProportionalUnits, int maxAllocIPD) {
        boolean bMaxIsSpecified = !this.ipd.getMaximum().getLength().isAuto();
        if (bMaxIsSpecified) {
            this.maxIPD = ipd.getMaximum().getLength().mvalue();
        } else {
            this.maxIPD = maxAllocIPD;
        }
        if (ipd.getOptimum().getLength().isAuto()) {
            this.optIPD = -1;
        } else {
            this.optIPD = ipd.getMaximum().getLength().mvalue();
        }
        if (ipd.getMinimum().getLength().isAuto()) {
            this.minIPD = -1;
        } else {
            this.minIPD = ipd.getMinimum().getLength().mvalue();
        }
        if (bHasProportionalUnits && this.optIPD < 0) {
            if (this.minIPD > 0) {
                if (bMaxIsSpecified) {
                    this.optIPD = (minIPD + maxIPD) / 2;
                } else {
                    this.optIPD = this.minIPD;
                }
            } else if (bMaxIsSpecified) {
                this.optIPD = this.maxIPD;
            } else {
                log.error("At least one of minimum, optimum, or maximum " + "IPD must be specified on table.");
                this.optIPD = this.maxIPD;
            }
        }
    }
