    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tt_main_pref);
        recID = "0";
        m_channels = new ArrayList<Channel>();
        this.m_adapter = new PostAdapter(this, R.layout.channelpref_row, m_channels);
        setListAdapter(this.m_adapter);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        Button btnSave = (Button) findViewById(R.id.BtnSave);
        btnSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TheMainActivity.tabContext.getTabHost().setCurrentTab(0);
            }
        });
        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView img_Selected = (ImageView) view.findViewById(R.id.imgSelected);
                ImageView img_Unselected = (ImageView) view.findViewById(R.id.imgUnselected);
                Channel selectedItem = (Channel) parent.getItemAtPosition(position);
                selectedItem.setFlag(selectedItem.getFlag() == 0 ? 1 : 0);
                if (selectedItem.getFlag() == 0) {
                    img_Selected.setVisibility(img_Selected.GONE);
                    img_Unselected.setVisibility(img_Unselected.VISIBLE);
                } else {
                    img_Selected.setVisibility(img_Selected.VISIBLE);
                    img_Unselected.setVisibility(img_Unselected.GONE);
                }
                img_Selected.refreshDrawableState();
                img_Unselected.refreshDrawableState();
                dbHelper = new DatabaseHelper(context);
                dbHelper.UpdateChannel(selectedItem);
            }
        });
        lv.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                } else if (!loading && ((totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold))) {
                    ChannelsPref.actionType = "getMoreChannels";
                    new LoadMoreEntries().execute(null, null, null);
                    loading = true;
                }
            }
        });
        ChannelsPref.actionType = "getChannels";
        new LoadMoreEntries().execute(null, null, null);
    }
