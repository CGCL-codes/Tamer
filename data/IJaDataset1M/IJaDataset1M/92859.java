package org.vizzini.math;

/**
 * Provides base functionality to scale numbers between two ranges.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.1
 */
public abstract class AbstractScaler implements IScaler {

    /** Maximum expected input value. */
    private double _maxInput;

    /** Maximum expected output value. */
    private double _maxOutput;

    /** Minimum expected input value. */
    private double _minInput;

    /** Minimum expected output value. */
    private double _minOutput;

    /**
     * Construct this object.
     *
     * @since  v0.1
     */
    public AbstractScaler() {
    }

    /**
     * Construct this object with the given parameters.
     *
     * @param  minInput   Minimum expected input.
     * @param  maxInput   Maximum expected input.
     * @param  minOutput  Minimum expected output.
     * @param  maxOutput  Maximum expected output.
     *
     * @since  v0.1
     */
    public AbstractScaler(double minInput, double maxInput, double minOutput, double maxOutput) {
        setMinInput(minInput);
        setMaxInput(maxInput);
        setMinOutput(minOutput);
        setMaxOutput(maxOutput);
    }

    /**
     * @see  org.vizzini.math.IScaler#getMaxInput()
     */
    public double getMaxInput() {
        return _maxInput;
    }

    /**
     * @see  org.vizzini.math.IScaler#getMaxOutput()
     */
    public double getMaxOutput() {
        return _maxOutput;
    }

    /**
     * @see  org.vizzini.math.IScaler#getMinInput()
     */
    public double getMinInput() {
        return _minInput;
    }

    /**
     * @see  org.vizzini.math.IScaler#getMinOutput()
     */
    public double getMinOutput() {
        return _minOutput;
    }

    /**
     * @see  org.vizzini.math.IScaler#scale(double[])
     */
    public double[] scale(double[] inputs) {
        double[] answer = new double[inputs.length];
        answer = scale(inputs, answer);
        return answer;
    }

    /**
     * @see  org.vizzini.math.IScaler#scale(double[], double[])
     */
    public double[] scale(double[] inputs, double[] outputs) {
        for (int i = 0; i < outputs.length; i++) {
            outputs[i] = scale(inputs[i]);
        }
        return outputs;
    }

    /**
     * @see  org.vizzini.math.IScaler#setMaxInput(double)
     */
    public void setMaxInput(double max) {
        _maxInput = max;
    }

    /**
     * @see  org.vizzini.math.IScaler#setMaxOutput(double)
     */
    public void setMaxOutput(double max) {
        _maxOutput = max;
    }

    /**
     * @see  org.vizzini.math.IScaler#setMinInput(double)
     */
    public void setMinInput(double min) {
        _minInput = min;
    }

    /**
     * @see  org.vizzini.math.IScaler#setMinOutput(double)
     */
    public void setMinOutput(double min) {
        _minOutput = min;
    }

    /**
     * @see  java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(" [");
        sb.append("_minInput=").append(_minInput);
        sb.append("_maxInput=").append(_maxInput);
        sb.append("_minOutput=").append(_minOutput);
        sb.append("_maxOutput=").append(_maxOutput);
        sb.append("]");
        return sb.toString();
    }

    /**
     * @see  org.vizzini.math.IScaler#unscale(double[])
     */
    public double[] unscale(double[] outputs) {
        double[] answer = new double[outputs.length];
        for (int i = 0; i < answer.length; i++) {
            answer[i] = unscale(outputs[i]);
        }
        return answer;
    }
}
