            @Override
            public void onClick(View v) {
                try {
                    URL url = new URL(_textLoadUrl.getText().toString());
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    Reader reader = new InputStreamReader(connection.getInputStream());
                    SqliteElement.importXmlStreamToDb(_configurationDialog.getDatabaseHelper().getWritableDatabase(), reader, ReplaceStrategy.REPLACE_EXISTING);
                    dismiss();
                    _configurationDialog.updateBookmarkView();
                } catch (MalformedURLException mfe) {
                    errorNotify("Improper URL given: " + _textLoadUrl.getText(), mfe);
                } catch (IOException ioe) {
                    errorNotify("I/O error reading configuration", ioe);
                } catch (SAXException e) {
                    errorNotify("XML or format error reading configuration", e);
                }
            }
