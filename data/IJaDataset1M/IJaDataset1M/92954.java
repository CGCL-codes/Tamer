package org.smartcc.replay;

import org.smartcc.*;

public class RecordingInterceptor extends AbstractInterceptor {

    public Object invoke(Invocation invocation) throws Throwable {
        Object result = null;
        Throwable throwable = null;
        try {
            result = next.invoke(invocation);
            return result;
        } catch (Throwable e) {
            throwable = e;
            throw e;
        } finally {
            Recorder.getInstance().record(invocation, result, throwable);
        }
    }
}
