    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.importexport);
        setTitle(R.string.import_export_settings);
        _textLoadUrl = (EditText) findViewById(R.id.textImportUrl);
        _textSaveUrl = (EditText) findViewById(R.id.textExportPath);
        File f = BCFactory.getInstance().getStorageContext().getExternalStorageDir(_configurationDialog, null);
        if (f == null) return;
        f = new File(f, "vnc_settings.xml");
        _textSaveUrl.setText(f.getAbsolutePath());
        try {
            _textLoadUrl.setText(f.toURL().toString());
        } catch (MalformedURLException e) {
        }
        Button export = (Button) findViewById(R.id.buttonExport);
        export.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    File f = new File(_textSaveUrl.getText().toString());
                    Writer writer = new OutputStreamWriter(new FileOutputStream(f, false));
                    SqliteElement.exportDbAsXmlToStream(_configurationDialog.getDatabaseHelper().getReadableDatabase(), writer);
                    writer.close();
                    dismiss();
                } catch (IOException ioe) {
                    errorNotify("I/O Exception exporting config", ioe);
                } catch (SAXException e) {
                    errorNotify("XML Exception exporting config", e);
                }
            }
        });
        ((Button) findViewById(R.id.buttonImport)).setOnClickListener(new View.OnClickListener() {

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
        });
    }
