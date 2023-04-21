        protected void execute() {
            if (this.started) {
                return;
            }
            this.started = true;
            this.cursor = new Cursor(Display.getDefault(), SWT.CURSOR_WAIT);
            getShell().setCursor(this.cursor);
            this.resultText.setText(ToolsUiPluginResourceBundle.test_WaitMessage);
            Thread thread = new Thread("HyadesHttpRequestTester") {

                public void run() {
                    String checkerReturn = null;
                    try {
                        HttpResponse response = new HttpExecutor().execute(CheckTestCaseDialog.this.request);
                        checkerReturn = NLS.bind(ToolsUiPluginResourceBundle.test_Response, (new String[] { response.getCode() + "", response.getContentType(), "" + response.getContentLength() }));
                        if (response.getDetail() != null) {
                            checkerReturn = checkerReturn + "\n\n" + response.getDetail();
                        }
                        if (response.getBody() != null) {
                            checkerReturn = checkerReturn + "\n\n" + response.getBody();
                        }
                    } catch (Throwable t) {
                        checkerReturn = ToolsUiPluginResourceBundle.test_Exception + "\n" + org.eclipse.tptp.platform.common.internal.util.CoreUtil.getStackTrace(t);
                    }
                    final String cr = checkerReturn;
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            testFinished(cr);
                        }
                    });
                }
            };
            thread.start();
        }
