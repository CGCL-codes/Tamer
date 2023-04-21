package cat.A65.updates.predictor;

import java.io.PrintStream;
import java.util.ArrayList;
import cat.A65.updates.Update;

public interface IPredictionStrategy {

    /**
	 * prints a comma-separated spread sheet with a histogram of the real values and
	 * the predicted values plus the error
	 * @param updates
	 * @param output
	 */
    void writePrediction(ArrayList<Update> updates, PrintStream output);
}
