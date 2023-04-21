    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        switch(operation) {
            case ADD:
                AOToolkit.add(s1, o1, l1, operands[0]);
                break;
            case SUBTRACT:
                AOToolkit.subtract(s1, o1, l1, operands[0]);
                break;
            case MULTIPLY:
                AOToolkit.multiply(s1, o1, l1, operands[0]);
                break;
            case DIVIDE:
                AOToolkit.divide(s1, o1, l1, operands[0]);
                break;
            case DERIVATE:
                AOToolkit.derivate(s1, o1, l1);
                break;
            case INTEGRATE:
                AOToolkit.integrate(s1, o1, l1);
                break;
            case INVERS:
                AOToolkit.invers(s1, o1, l1);
                break;
            case NEG:
                AOToolkit.neg(s1, o1, l1);
                break;
            case POW:
                AOToolkit.pow(s1, o1, l1, operands[0]);
                break;
            case SQRT:
                AOToolkit.sqrt(s1, o1, l1);
                break;
            case EXP:
                AOToolkit.exp(s1, o1, l1);
                break;
            case LOG:
                AOToolkit.log(s1, o1, l1);
                break;
            case TO_dB:
                AOToolkit.todB(s1, o1, l1);
                break;
            case FROM_dB:
                AOToolkit.fromdB(s1, o1, l1);
                break;
            case MEAN:
                AOToolkit.smooth(s1, o1, l1, (int) operands[0]);
                break;
            case RMS:
                AOToolkit.smoothRms(s1, o1, l1, (int) operands[0]);
                break;
        }
    }
