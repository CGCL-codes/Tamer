package au.org.tpac.portal.gwt.client.service;

import java.util.List;
import au.org.tpac.portal.gwt.client.data.GuiListItem;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface TagServiceAsync.
 */
public interface TagServiceAsync {

    /**
     * Fetch tags.
     *
     * @param classification the classification
     * @param callback the callback
     * @return the request builder
     */
    RequestBuilder fetchTags(String classification, AsyncCallback<List<GuiListItem>> callback);

    /**
     * Delete tag.
     *
     * @param TagId the tag id
     * @param callback the callback
     */
    void deleteTag(int TagId, AsyncCallback<Integer> callback);

    /**
     * Insert tag.
     *
     * @param classification the classification
     * @param tagName the tag name
     * @param callback the callback
     */
    void insertTag(String classification, String tagName, AsyncCallback<Integer> callback);

    /**
	 * Update tag.
	 *
	 * @param intTagId the int tag id
	 * @param name the name
	 * @param callback the callback
	 */
    void updateTag(int intTagId, String name, String classification, AsyncCallback<Integer> callback);
}
