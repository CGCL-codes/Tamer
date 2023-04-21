package jaga.experiment;

import jaga.SampleData;

/** Used to plug-in to experiments so that their generateInput method can be configurable.
 *
 * @author  mmg20
 */
public interface TestPatternGenerator extends java.io.Serializable {

    /** @return Test Pattern with defined width. */
    public SampleData[] getPattern(int width);

    /** Seeds the random number generator to repeat a previous random pattern */
    public void setRandomSeed(long seed);
}
