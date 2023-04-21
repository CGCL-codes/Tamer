package edu.mta.ok.nworkshop.similarity;

import edu.mta.ok.nworkshop.model.MovieIndexedModelRatings;
import edu.mta.ok.nworkshop.model.UserIndexedModelRatings;
import edu.mta.ok.nworkshop.utils.FileUtils;

/**
 * Calculates the similarity between two movies using the interpolation similarity model suggested in 
 * the following Bellkor abstract: <a href="http://public.research.att.com/~volinsky/netflix/cfworkshop.pdf">Improved Neighborhood-based Collaborative Filtering</a>
 * on section 4.
 * 
 * The class uses the raw data given by Netflix in order to calculate the similarities values. 
 * 
 * @see UserIndexedModelRatings
 * @see MovieIndexedModelRatings
 */
public class InterpolationSimilarityRawScores extends InterpolationSimilarityAbstract {

    private static boolean loadedFromFile = false;

    private InterpolationSimilarityRawScores() {
        super();
    }

    public InterpolationSimilarityRawScores(String movieModelFileName, String userModelFileName) {
        super(new MovieIndexedModelRatings(movieModelFileName), new UserIndexedModelRatings(userModelFileName));
    }

    public InterpolationSimilarityRawScores(String movieModelFileName, String userModelFileName, String userIndicesFileName) {
        super(new MovieIndexedModelRatings(movieModelFileName), new UserIndexedModelRatings(userModelFileName, userIndicesFileName));
    }

    /**
	 * Builds the full similarity model by calculating similarity between every two movies in the model.
	 * 
	 * @throws UnsupportedOperationException in case the similarity model had been loaded from a file using
	 * {@link #getSimilarityFromFile(String, boolean)}
	 */
    @Override
    public void calculateSimilarities() {
        if (loadedFromFile) {
            throw new UnsupportedOperationException("Can't calculate similarity on class loaded from file");
        } else {
            super.calculateSimilarities();
        }
    }

    @Override
    protected double getMovieRating(int userInd, int position) {
        return ((byte[]) userIndexedModel.getUserRatingsByIndex(userInd))[position];
    }

    /**
	 * Creates a new instance of the class by loading the similarity model from a given file
	 * 
	 * REMARK: Notice that no calculation will be available on the return instance, trying to call
	 * "calculateSimilarities" will throw an exception.
	 * 
	 * @see #calculateSimilarities() 
	 * @param fileName a file containing the similarity model (a double matrix)
	 * @param floatModel mark if the loaded model holds float data or double data
	 * @return a new instance of the class with a similarity model loaded from a given file
	 */
    public static InterpolationSimilarityRawScores getSimilarityFromFile(String fileName, boolean floatModel) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        floatSimilarityModel = floatModel;
        InterpolationSimilarityRawScores retVal = new InterpolationSimilarityRawScores();
        if (!floatModel) {
            retVal.setSimilarities((double[][]) FileUtils.loadDataFromFile(fileName));
        } else {
            retVal.setSimilarities((float[][]) FileUtils.loadDataFromFile(fileName));
        }
        retVal.setLoadedFromFile(true);
        return retVal;
    }

    private void setLoadedFromFile(boolean loadedFromFile) {
        InterpolationSimilarityRawScores.loadedFromFile = loadedFromFile;
    }
}
