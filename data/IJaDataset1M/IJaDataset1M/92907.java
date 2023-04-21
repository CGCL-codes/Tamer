package org.dcw.lgt.tools;

import org.apache.log4j.Logger;

/**
* @version $LastChangedRevision: 38 $
* @author $LastChangedBy: tlemaire59 $
* Date $LastChangedDate: 2008-12-26 13:13:22 -0500 (Fri, 26 Dec 2008) $
* Id $Id: LoggerLGT.java 38 2008-12-26 18:13:22Z tlemaire59 $
* HeadURL $HeadURL: http://lgt.svn.sourceforge.net/svnroot/lgt/trunk/org-dcw-lgt-tools/src/main/java/org/dcw/lgt/tools/LoggerLGT.java $
* 
**/
public class LoggerLGT {

    private static org.apache.log4j.Logger log = null;

    /**
     * Get instance of logger
     * @return
     */
    public static Logger getInstance() {
        if (log == null) {
            log = Logger.getLogger(LoggerLGT.class);
        }
        return log;
    }

    /**
     * Don't create logger
     */
    private LoggerLGT() {
    }
}
