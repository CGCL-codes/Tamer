package org.openscience.cdk.io.formats;

/**
 * @cdk.module test-ioformats
 */
public class Aces2FormatTest extends ChemFormatMatcherTest {

    public Aces2FormatTest() {
        super.setChemFormatMatcher((IChemFormatMatcher) Aces2Format.getInstance());
    }
}
