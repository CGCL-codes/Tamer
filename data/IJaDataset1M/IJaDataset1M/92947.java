package checker.repository;

import java.util.ArrayList;
import checker.filepackage.FilePackage;

public class RepositoryFactory {

    /**
	 * Gets a list of supported repository type ID:s. (like cvs...)
	 * 
	 * @return
	 */
    public static ArrayList<String> getSupportedRepositoryTypeIDs() {
        return null;
    }

    /**
	 * Creates a new Repository object that represents a desired repository.
	 * 
	 * @param repositoryTypeID
	 *            ID of the repository type
	 * @param parameters
	 *            Variable list of parameters that are needed to open the
	 *            repository.
	 * @return
	 */
    public static FilePackage createRepository(String repositoryTypeID, String... parameters) {
        return null;
    }

    /**
	 * Gets a list of parameter descriptions that the repository requires. Must
	 * be in the same order as when supplied back to createRepository.
	 * 
	 * @param repositoryID
	 * @return Parameter descriptions (will be shown in the GUI)
	 */
    public static ArrayList<String> getParameterDescriptions(String repositoryID) throws Exception {
        return null;
    }
}
