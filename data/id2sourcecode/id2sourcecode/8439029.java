    public double cubicInterpolation(double alphai, double linearFunctionInAlphai, double linearFunctionDerivativeInAlphai, double alphaiMinus1, double linearFunctionInAlphaiMinus1, double linearFunctionDerivativeInAlphaiMinus1, double a, double b) {
        if (alphai < alphaiMinus1) {
            this.alphaTemporal = alphai;
            this.linearFunctionInAlphaTemporal = linearFunctionInAlphai;
            this.linearFunctionDerivativeInAlphaTemporal = linearFunctionDerivativeInAlphai;
            alphai = alphaiMinus1;
            linearFunctionInAlphai = linearFunctionInAlphaiMinus1;
            linearFunctionDerivativeInAlphai = linearFunctionDerivativeInAlphaiMinus1;
            alphaiMinus1 = this.alphaTemporal;
            linearFunctionInAlphaiMinus1 = this.linearFunctionInAlphaTemporal;
            linearFunctionDerivativeInAlphaiMinus1 = this.linearFunctionDerivativeInAlphaTemporal;
        }
        this.d1 = linearFunctionDerivativeInAlphaiMinus1 + linearFunctionDerivativeInAlphai - 3 * ((linearFunctionInAlphaiMinus1 - linearFunctionInAlphai) / (alphaiMinus1 - alphai));
        this.d2 = Math.sqrt(Math.abs(Math.pow(d1, 2) - linearFunctionDerivativeInAlphaiMinus1 * linearFunctionDerivativeInAlphai));
        this.alphaiplus1 = alphai - (alphai - alphaiMinus1) * ((linearFunctionDerivativeInAlphai + d2 - d1) / (linearFunctionDerivativeInAlphai - linearFunctionDerivativeInAlphaiMinus1 + 2 * d2));
        if (alphaiplus1 < a) {
            alphaiplus1 = a;
        }
        if (alphaiplus1 > b) {
            alphaiplus1 = b;
        }
        if (Math.abs(alphaiplus1 - alphai) < 0.000000001) {
            alphaiplus1 = (alphaiMinus1 + alphai) / 2;
        } else {
            if (alphaiplus1 < (alphai - 9 * (alphai - alphaiMinus1) / 10)) {
                alphaiplus1 = (alphaiMinus1 + alphai) / 2;
                ;
            }
        }
        return alphaiplus1;
    }
