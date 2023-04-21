package net.openchrom.chromatogram.msd.converter.supplier.amdis.model;

import net.openchrom.chromatogram.msd.model.core.AbstractRegularLibraryIon;
import net.openchrom.chromatogram.msd.model.core.IIon;
import net.openchrom.chromatogram.msd.model.exceptions.AbundanceLimitExceededException;
import net.openchrom.chromatogram.msd.model.exceptions.IonLimitExceededException;
import net.openchrom.chromatogram.msd.model.exceptions.IonIsNullException;

public class AmdisIon extends AbstractRegularLibraryIon implements IAmdisIon {

    /**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
    private static final long serialVersionUID = 6539944532255720513L;

    public AmdisIon(float ion, float abundance) throws AbundanceLimitExceededException, IonLimitExceededException {
        super(ion, abundance);
    }

    public AmdisIon(float ion) throws IonLimitExceededException {
        super(ion);
    }

    public AmdisIon(IIon ion) throws AbundanceLimitExceededException, IonLimitExceededException, IonIsNullException {
        super(ion);
    }
}
