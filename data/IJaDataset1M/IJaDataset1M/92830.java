package dimyoux.engine.core.signals;

/**
 * Signal interface for change of status bar visibility.
 */
public interface IStatusBarDisabled extends ISignal {

    /**
	 * Called when the status bar is disabled or enabled.
	 * @param disabled True if the status bar is disabled, false otherwise.
	 */
    public void onStatusBarDisabled(Boolean disabled);
}
