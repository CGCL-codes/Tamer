        public Object call(Context context, Scriptable scope, Scriptable thisObj, Object[] args) {
            if (args.length < EXPECTED_NUM_ARGS) {
                throw Context.reportRuntimeError("Bad number of parameters for function" + " toString: expected " + EXPECTED_NUM_ARGS + ", got " + args.length);
            }
            Value thisValue = makeValueFromJsval(context, thisObj);
            ExceptionOrReturnValue returnValue = JavaObject.getReturnFromJavaMethod(context, HtmlUnitSessionHandler.this, sessionData.getChannel(), TO_STRING_DISPATCH_ID, thisValue, EMPTY_VALUES);
            return HtmlUnitSessionHandler.this.makeJsvalFromValue(context, returnValue.getReturnValue());
        }
