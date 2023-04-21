package net.sourceforge.processdash.data.compiler.function;

import java.util.Iterator;
import net.sourceforge.processdash.data.ListData;
import net.sourceforge.processdash.data.compiler.AbstractFunction;
import net.sourceforge.processdash.data.compiler.ExpressionContext;

public class List extends AbstractFunction {

    /** Perform a procedure call.
     *
     * This method <b>must</b> be thread-safe.
     */
    public Object call(java.util.List arguments, ExpressionContext context) {
        ListData result = new ListData();
        Iterator i = collapseLists(arguments, 0).iterator();
        while (i.hasNext()) result.add(i.next());
        return result;
    }
}
