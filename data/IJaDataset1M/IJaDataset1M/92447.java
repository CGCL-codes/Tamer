package net.pms.formats;

public class FLAC extends OGG {

    public FLAC() {
        type = AUDIO;
        secondaryFormat = new AudioAsVideo();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String[] getId() {
        return new String[] { "flac", "mlp", "fla" };
    }
}
