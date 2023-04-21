    public FGTrimAxis(FGFDMExec fdex, FGInitialCondition ic, short st, short ctrl) {
        super();
        fdmex = fdex;
        fgic = ic;
        eState = st;
        eControl = ctrl;
        switch(eState) {
            case State.tUdot:
            case State.tVdot:
            case State.tWdot:
                tolerance = DEFAULT_TOLERANCE;
                break;
            case State.tQdot:
            case State.tPdot:
            case State.tRdot:
                tolerance = DEFAULT_TOLERANCE / 10;
                break;
            case State.tHmgt:
                tolerance = 0.01;
                break;
            case State.tNlf:
                state_target = 1.0;
                tolerance = 1E-5;
                break;
            case State.tAll:
                break;
        }
        solver_eps = tolerance;
        switch(eControl) {
            case Control.tThrottle:
                control_min = 0;
                control_max = 1;
                control_value = 0.5;
                break;
            case Control.tBeta:
                control_min = -30 * FGUnitConverter.degtorad;
                control_max = 30 * FGUnitConverter.degtorad;
                control_convert = FGUnitConverter.radtodeg;
                break;
            case Control.tAlpha:
                control_min = fdmex.GetAerodynamics().GetAlphaCLMin();
                control_max = fdmex.GetAerodynamics().GetAlphaCLMax();
                if (control_max <= control_min) {
                    control_max = 20 * FGUnitConverter.degtorad;
                    control_min = -5 * FGUnitConverter.degtorad;
                }
                control_value = (control_min + control_max) / 2;
                control_convert = FGUnitConverter.radtodeg;
                solver_eps = tolerance / 100;
                break;
            case Control.tPitchTrim:
            case Control.tElevator:
            case Control.tRollTrim:
            case Control.tAileron:
            case Control.tYawTrim:
            case Control.tRudder:
                control_min = -1;
                control_max = 1;
                state_convert = FGUnitConverter.radtodeg;
                solver_eps = tolerance / 100;
                break;
            case Control.tAltAGL:
                control_min = 0;
                control_max = 30;
                control_value = fdmex.GetPropagate().GetDistanceAGL();
                solver_eps = tolerance / 100;
                break;
            case Control.tTheta:
                control_min = fdmex.GetPropagate().GetEuler().GetEntry(EulerAngels.eTht) - 5 * FGUnitConverter.degtorad;
                control_max = fdmex.GetPropagate().GetEuler().GetEntry(EulerAngels.eTht) + 5 * FGUnitConverter.degtorad;
                state_convert = FGUnitConverter.radtodeg;
                break;
            case Control.tPhi:
                control_min = fdmex.GetPropagate().GetEuler().GetEntry(EulerAngels.ePhi) - 30 * FGUnitConverter.degtorad;
                control_max = fdmex.GetPropagate().GetEuler().GetEntry(EulerAngels.ePhi) + 30 * FGUnitConverter.degtorad;
                state_convert = FGUnitConverter.radtodeg;
                control_convert = FGUnitConverter.radtodeg;
                break;
            case Control.tGamma:
                solver_eps = tolerance / 100;
                control_min = -80 * FGUnitConverter.degtorad;
                control_max = 80 * FGUnitConverter.degtorad;
                control_convert = FGUnitConverter.radtodeg;
                break;
            case Control.tHeading:
                control_min = fdmex.GetPropagate().GetEuler().GetEntry(EulerAngels.ePsi) - 30 * FGUnitConverter.degtorad;
                control_max = fdmex.GetPropagate().GetEuler().GetEntry(EulerAngels.ePsi) + 30 * FGUnitConverter.degtorad;
                state_convert = FGUnitConverter.radtodeg;
                break;
        }
        Debug(0);
    }
