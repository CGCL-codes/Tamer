            public void run() {
                IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
                if (selection.isEmpty()) {
                    MessageDialog.openInformation(parent.getShell(), "Article Description", "Unable to preview, please select a search result");
                    return;
                }
                ArrayList validNodes = new ArrayList();
                for (Iterator iterator = selection.iterator(); iterator.hasNext(); ) {
                    NewCanalNode domain = (NewCanalNode) iterator.next();
                    if (domain.getType() == CONSTANTS.TYPE_FEED_READ_ARTICLE || domain.getType() == CONSTANTS.TYPE_FEED_UNREAD_ARTICLE) {
                        validNodes.add(domain);
                    }
                }
                NewCanalNode[] newnodes = new NewCanalNode[validNodes.size()];
                validNodes.toArray(newnodes);
                String defaultpdf = null;
                String temp = System.getProperty("java.io.tmpdir");
                Format formatter = new SimpleDateFormat("'Time'_HH_mm_ss_'Day'_dd_MM_yyyy");
                Date date = new Date();
                String s = formatter.format(date);
                defaultpdf = temp + s + ".pdf";
                ArrayList list = FeedWriter.writePDFFeed(newnodes, SearchCatcher.getUrlToArticleVector(), defaultpdf, PrefPageOne.getIntValue(CONSTANTS.PREF_SORT_ORDER));
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(defaultpdf);
                    PDFDocumentFactory dg = new PDFDocumentFactory(fos, false, true);
                    dg.createPDF(list);
                    fos.close();
                    BrowserView.setUrlInNewTab(defaultpdf, null, true);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
