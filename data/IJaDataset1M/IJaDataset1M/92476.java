package uk.ac.osswatch.simal.wicket.panel.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.data.SortableDocumentDataProvider;
import uk.ac.osswatch.simal.wicket.markup.html.repeater.data.table.LinkPropertyColumn;

/**
 * A simple panel for listing a set of any IDoapResources. 
 */
public abstract class DocumentSetPanel extends AbstractEditableResourcesPanel<IDocument> {

    public static final Logger LOGGER = LoggerFactory.getLogger(DocumentSetPanel.class);

    private static final long serialVersionUID = -932080365392667144L;

    private static final int MAX_ROWS_PER_PAGE = 10;

    private IProject project;

    private Set<IDocument> documents;

    /**
   * Create a panel that lists all homepages in the repository.
   * 
   * @param id
   *          the wicket ID for the component
   * @param title
   *          the title if this list
   * @param numberOfPages
   *          the number of homepages to display per page
   * @throws SimalRepositoryException
   */
    public DocumentSetPanel(String id, String title, Set<IDocument> resources, IProject project) {
        super(id, title);
        this.project = project;
        this.documents = (Set<IDocument>) resources;
        addAddDoapResourcePanel(new AddIResourcePanel("addDocumentPanel", this));
        addDocumentsList();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addDocumentsList() {
        List<IColumn<?>> columns = new ArrayList<IColumn<?>>();
        columns.add(new LinkPropertyColumn(new Model<String>("Name"), "label", "label"));
        PropertyColumn<IDocument> urlColumn = new PropertyColumn<IDocument>(new Model<String>("URL"), "url", "url") {

            private static final long serialVersionUID = -3063052337733586041L;

            @Override
            public void populateItem(Item<ICellPopulator<IDocument>> cellItem, String componentId, IModel<IDocument> model) {
                String label = "";
                if (model != null) {
                    label = model.getObject().getURI();
                }
                cellItem.add(new Label(componentId, label));
            }

            public String getCssClass() {
                return "visiblecell";
            }
        };
        columns.add(urlColumn);
        PropertyColumn<IDocument> deleteColumn = new PropertyColumn<IDocument>(new Model<String>("Delete"), "name", "name") {

            private static final long serialVersionUID = -3063052337733586041L;

            public void populateItem(Item<ICellPopulator<IDocument>> cellItem, String componentId, IModel<IDocument> model) {
                AjaxFallbackLink<IDocument> deleteLink = new AjaxFallbackLink<IDocument>(componentId, model) {

                    private static final long serialVersionUID = 876069018792653905L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        processDeleteOnClick(target, getModel().getObject());
                    }

                    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
                        String linkText = "<a href=\"#\">Delete</a>";
                        replaceComponentTagBody(markupStream, openTag, linkText);
                    }
                };
                cellItem.add(deleteLink);
            }
        };
        columns.add(deleteColumn);
        SortableDocumentDataProvider<IDocument> dataProvider = new SortableDocumentDataProvider<IDocument>(documents);
        dataProvider.setSort(SortableDocumentDataProvider.SORT_PROPERTY_NAME, true);
        add(new AjaxFallbackDefaultDataTable("dataTable", columns, dataProvider, MAX_ROWS_PER_PAGE));
    }

    /**
   * Add a resource to the list being displayed. This method does not add the
   * resource to the underlying data storage mechanism, it only adds it to the
   * GUI.
   * 
   * @param iFoafResource
   */
    public void addToDisplayList(IDocument iFoafResource) {
        this.documents.add(iFoafResource);
    }

    protected IProject getProject() {
        return this.project;
    }

    private void processDeleteOnClick(AjaxRequestTarget target, IDocument document) {
        try {
            processDelete(document);
        } catch (SimalException e) {
            LOGGER.warn("Failed to delete resource " + document);
        }
        target.addComponent(this);
    }

    /**
   * Remove a resource from the list being displayed. This method does not delete 
   * the resource from the underlying data storage mechanism, but only from the GUI.
   * 
   * @param iDoapResource
   */
    public void delete(IDocument iDoapResource) {
        this.documents.remove(iDoapResource);
    }

    public abstract void addToModel(IDocument document) throws SimalException;

    public abstract void removeFromModel(IDocument document) throws SimalException;

    public void processDelete(IDocument document) throws SimalException {
        delete(document);
        removeFromModel(document);
    }
}
