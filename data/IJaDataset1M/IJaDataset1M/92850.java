package org.gjt.sp.util;

import java.util.EventListener;

/**
 * A work thread execution progress listener.
 * @since jEdit 2.6pre1
 */
public interface WorkThreadProgressListener extends EventListener {

    void statusUpdate(WorkThreadPool threadPool, int threadIndex);

    void progressUpdate(WorkThreadPool threadPool, int threadIndex);
}
