package gov.nasa.jpf.complexcoverage;

import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.search.Search;

public class TraceListener extends ListenerAdapter {

    /**
	 * Print out the minimal test cases discovered by Debug.storeMinimalTraceIf().
	 * The output will appear in the file(s) specified by Debug.storeMinimalTraceIf().
	 */
    public void searchFinished(Search search) {
        JPF_gov_nasa_jpf_complexcoverage_Debug.writeTraces();
    }

    ;
}
