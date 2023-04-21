package org.berlin.wicket.funcs;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.berlin.dao.RequestActivityLog;
import org.berlin.model.RequestActivityModel;

public class ActivityLogPage extends WebPage {

    private static final long serialVersionUID = 1L;

    public ActivityLogPage() {
        super();
    }

    /**
     * Use onIntialize to add components to the page. Avoid component creation in the constructor.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();
        final LoadableListModel<RequestActivityModel> activityModelList = new LoadableListModel<RequestActivityModel>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected List<? extends RequestActivityModel> load() {
                final RequestActivityLog log = new RequestActivityLog();
                return log.list();
            }
        };
        final ListView<RequestActivityModel> activityListView = new ListView<RequestActivityModel>("activityListView", activityModelList) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<RequestActivityModel> item) {
                final RequestActivityModel itemBean = item.getModelObject();
                item.setModel(new CompoundPropertyModel<RequestActivityModel>(itemBean));
                item.add(new Label("timestamp"));
                item.add(new Label("requestURL"));
            }
        };
        this.add(activityListView);
    }

    @Override
    protected void onBeforeRender() {
        final HttpServletRequest request = (HttpServletRequest) getRequest().getContainerRequest();
        final RequestActivityLog log = new RequestActivityLog();
        log.log(request);
        super.onBeforeRender();
    }
}
