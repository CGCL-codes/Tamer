    public FGTrimAnalysisControl(FGFDMExec fdex, FGInitialCondition ic, short ctrl) {
        super();
        double degtorad = FGUnitConverter.degtorad, radtodeg = FGUnitConverter.radtodeg;
        fdmex = fdex;
        fgic = ic;
        eTaState = TaState.taAll;
        eTaControl = ctrl;
        control_tolerance = FGTrimAxis.DEFAULT_TOLERANCE;
        switch(eTaControl) {
            case TaControl.taThrottle:
                control_min = 0;
                control_max = 1;
                control_step = 0.2;
                control_initial_value = 0.5;
                control_value = control_initial_value;
                control_name = "Throttle (cmd,norm)";
                break;
            case TaControl.taBeta:
                control_min = -30 * degtorad;
                control_max = 30 * degtorad;
                control_step = 1 * degtorad;
                control_convert = radtodeg;
                break;
            case TaControl.taAlpha:
                control_min = fdmex.GetAerodynamics().GetAlphaCLMin();
                control_max = fdmex.GetAerodynamics().GetAlphaCLMax();
                if (control_max <= control_min) {
                    control_max = 20 * degtorad;
                    control_min = -5 * degtorad;
                }
                control_step = 1 * degtorad;
                control_initial_value = (control_min + control_max) / 2;
                control_value = control_initial_value;
                control_convert = radtodeg;
                break;
            case TaControl.taPitchTrim:
                control_name = "Pitch Trim (cmd,norm)";
                control_min = -1;
                control_max = 1;
                control_step = 0.1;
                state_convert = radtodeg;
                break;
            case TaControl.taElevator:
                control_name = "Elevator (cmd,norm)";
                control_min = -1;
                control_max = 1;
                control_step = 0.1;
                state_convert = radtodeg;
                break;
            case TaControl.taRollTrim:
                control_name = "Roll Trim (cmd,norm)";
                control_min = -1;
                control_max = 1;
                control_step = 0.1;
                state_convert = radtodeg;
                break;
            case TaControl.taAileron:
                control_name = "Ailerons (cmd,norm)";
                control_min = -1;
                control_max = 1;
                control_step = 0.1;
                state_convert = radtodeg;
                break;
            case TaControl.taYawTrim:
                control_name = "Yaw Trim (cmd,norm)";
                control_min = -1;
                control_max = 1;
                control_step = 0.1;
                state_convert = radtodeg;
                break;
            case TaControl.taRudder:
                control_name = "Rudder (cmd,norm)";
                control_min = -1;
                control_max = 1;
                control_step = 0.1;
                state_convert = radtodeg;
                break;
            case TaControl.taAltAGL:
                control_name = "Altitude (ft)";
                control_min = 0;
                control_max = 30;
                control_step = 2;
                control_initial_value = fdmex.GetPropagate().GetDistanceAGL();
                control_value = control_initial_value;
                break;
            case TaControl.taPhi:
                control_name = "Phi (rad)";
                control_min = fdmex.GetPropagate().GetEuler(EulerAngels.ePhi) - 30 * degtorad;
                control_max = fdmex.GetPropagate().GetEuler(EulerAngels.ePhi) + 30 * degtorad;
                control_step = 1 * degtorad;
                state_convert = radtodeg;
                control_convert = radtodeg;
                break;
            case TaControl.taTheta:
                control_name = "Theta (rad)";
                control_min = fdmex.GetPropagate().GetEuler(EulerAngels.eTht) - 5 * degtorad;
                control_max = fdmex.GetPropagate().GetEuler(EulerAngels.eTht) + 5 * degtorad;
                control_step = 1 * degtorad;
                state_convert = radtodeg;
                break;
            case TaControl.taHeading:
                control_name = "Heading (rad)";
                control_min = fdmex.GetPropagate().GetEuler(EulerAngels.ePsi) - 30 * degtorad;
                control_max = fdmex.GetPropagate().GetEuler(EulerAngels.ePsi) + 30 * degtorad;
                control_step = 1 * degtorad;
                state_convert = radtodeg;
                break;
            case TaControl.taGamma:
                control_name = "Gamma (rad)";
                control_min = -80 * degtorad;
                control_max = 80 * degtorad;
                control_step = 1 * degtorad;
                control_convert = radtodeg;
                break;
        }
        Debug(0);
    }
