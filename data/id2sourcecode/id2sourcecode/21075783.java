            public void run() {
                NavigationView navigationView = (NavigationView) CUBRIDAdvisor.getView(NavigationView.ID);
                CTabItem cTabItem = navigationView.tabFolder.getSelection();
                if (cTabItem.getControl() instanceof FunctionTab) {
                    ExecuteCaseView view = (ExecuteCaseView) CUBRIDAdvisor.getView(ExecuteCaseView.ID);
                    TableViewer tableViewer = view.getCas().getRs().getTableViewer();
                    StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
                    if (selection.size() > 0) {
                        boolean openConfirm = MessageDialog.openConfirm(getSite().getShell(), "Are you sure?", "Do you really want to save answers?");
                        if (openConfirm) {
                            for (Iterator iterator = selection.iterator(); iterator.hasNext(); ) {
                                ExtResult extResult = (ExtResult) iterator.next();
                                String rs = extResult.getResult();
                                rs = rs.substring(0, rs.lastIndexOf(".")) + ".result";
                                rs = rs.replaceAll("\\\\", "/");
                                String willReppace = "/" + TestUtil.getAnswer4SQLAndOther(rs);
                                String answer = rs.replaceAll("/cases", willReppace);
                                answer = answer.replaceAll(".result", ".answer");
                                String saveDirectory = answer.substring(0, answer.indexOf(willReppace)) + "/" + willReppace;
                                if (!new File(saveDirectory).exists()) {
                                    new File(saveDirectory).mkdir();
                                }
                                try {
                                    FileUtils.copyFile(new File(rs), new File(answer));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            MessageDialog.openInformation(getSite().getShell(), "Save answer", "Answers have been saved");
                        }
                    }
                } else if (cTabItem.getControl() instanceof ResultTreeTab) {
                    ResultListView view = (ResultListView) CUBRIDAdvisor.getView(ResultListView.ID);
                    Composite composite2 = view.getComposite();
                    if (composite2 instanceof ShowListSummary) {
                        ShowListSummary showListSummary = (ShowListSummary) composite2;
                        ShowSummary showSummary = showListSummary.getShwoSummary();
                        TableViewer tableViewer = showSummary.getTableViewer();
                        SummaryInfo parentSummaryInfo = showSummary.getParentSummaryInfo();
                        StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
                        if (selection.size() > 0) {
                            String firstElement = (String) selection.getFirstElement();
                            if (firstElement.endsWith(":ok") || firstElement.endsWith(":nok") || firstElement.endsWith(":norun")) {
                                boolean openConfirm = MessageDialog.openConfirm(getSite().getShell(), "Are you sure?", "Do you really want to save answers?");
                                if (openConfirm) {
                                    for (Iterator iterator = selection.iterator(); iterator.hasNext(); ) {
                                        String rs = (String) iterator.next();
                                        boolean success = true;
                                        if (rs.endsWith(":nok")) {
                                            success = false;
                                        }
                                        String[] strings = rs.split("::runTimes::");
                                        rs = strings[1];
                                        rs = rs.substring(0, rs.lastIndexOf(".")) + ".result";
                                        String resultPath = rs;
                                        if (!success) {
                                            resultPath = PropertiesUtil.getValue("local.path") + parentSummaryInfo.getResultDir().substring(parentSummaryInfo.getLocalPath().length()) + "/" + rs.substring(rs.lastIndexOf("/") + 1, rs.length());
                                        }
                                        rs = rs.replaceAll("\\\\", "/");
                                        String willReppace = "/" + TestUtil.getAnswer4SQLAndOther(rs);
                                        String answer = rs.replaceAll("/cases", willReppace);
                                        answer = answer.replaceAll(".result", ".answer");
                                        String saveDirectory = answer.substring(0, answer.indexOf(willReppace)) + "/" + willReppace;
                                        if (!new File(saveDirectory).exists()) {
                                            new File(saveDirectory).mkdir();
                                        }
                                        try {
                                            FileUtils.copyFile(new File(resultPath), new File(answer));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    MessageDialog.openInformation(getSite().getShell(), "Save answer", "Answers have been saved");
                                }
                            }
                        }
                    }
                }
            }
