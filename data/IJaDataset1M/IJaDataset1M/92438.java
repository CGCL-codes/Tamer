package org.archive.crawler.postprocessor;

/**
 * A specialized ContentBasedWaitEvaluator. Comes preset with a regular 
 * expression that matches text documents. <code>^text/.*$</code>
 *
 * @author Kristinn Sigurdsson
 * 
 * @see org.archive.crawler.postprocessor.ContentBasedWaitEvaluator
 */
public class TextWaitEvaluator extends ContentBasedWaitEvaluator {

    private static final long serialVersionUID = -328402266684681632L;

    protected static final Long DEFAULT_INITIAL_WAIT_INTERVAL = new Long(43200);

    protected static final String DEFAULT_CONTENT_REGEXPR = "^text/.*$";

    /**
     * Constructor
     * 
     * @param name The name of the module
     */
    public TextWaitEvaluator(String name) {
        super(name, "Evaluates how long to wait before fetching a URI again. " + "Only handles CrawlURIs whose content type indicates a " + "text document (^text/.*$).\n" + "Typically, this processor should be in the post processing " + "chain. It will pass if another wait evaluator has already " + "processed the CrawlURI.", DEFAULT_CONTENT_REGEXPR, DEFAULT_INITIAL_WAIT_INTERVAL, DEFAULT_MAX_WAIT_INTERVAL, DEFAULT_MIN_WAIT_INTERVAL, DEFAULT_UNCHANGED_FACTOR, DEFAULT_CHANGED_FACTOR);
    }
}
