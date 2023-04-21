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
