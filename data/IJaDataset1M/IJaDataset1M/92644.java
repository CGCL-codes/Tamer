package com.liferay.portlet.messageboards.service;

/**
 * <a href="MBCategoryServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * <code>com.liferay.portlet.messageboards.service.MBCategoryService</code>
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.messageboards.service.MBCategoryService
 *
 */
public class MBCategoryServiceUtil {

    private static MBCategoryService _service;

    public static com.liferay.portlet.messageboards.model.MBCategory addCategory(long plid, long parentCategoryId, java.lang.String name, java.lang.String description, boolean addCommunityPermissions, boolean addGuestPermissions) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        return _service.addCategory(plid, parentCategoryId, name, description, addCommunityPermissions, addGuestPermissions);
    }

    public static com.liferay.portlet.messageboards.model.MBCategory addCategory(long plid, long parentCategoryId, java.lang.String name, java.lang.String description, java.lang.String[] communityPermissions, java.lang.String[] guestPermissions) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        return _service.addCategory(plid, parentCategoryId, name, description, communityPermissions, guestPermissions);
    }

    public static void deleteCategory(long categoryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        _service.deleteCategory(categoryId);
    }

    public static com.liferay.portlet.messageboards.model.MBCategory getCategory(long categoryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        return _service.getCategory(categoryId);
    }

    public static java.util.List<com.liferay.portlet.messageboards.model.MBCategory> getCategories(long groupId, long parentCategoryId, int start, int end) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        return _service.getCategories(groupId, parentCategoryId, start, end);
    }

    public static int getCategoriesCount(long groupId, long parentCategoryId) throws com.liferay.portal.SystemException, java.rmi.RemoteException {
        return _service.getCategoriesCount(groupId, parentCategoryId);
    }

    public static void subscribeCategory(long categoryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        _service.subscribeCategory(categoryId);
    }

    public static void unsubscribeCategory(long categoryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        _service.unsubscribeCategory(categoryId);
    }

    public static com.liferay.portlet.messageboards.model.MBCategory updateCategory(long categoryId, long parentCategoryId, java.lang.String name, java.lang.String description, boolean mergeWithParentCategory) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        return _service.updateCategory(categoryId, parentCategoryId, name, description, mergeWithParentCategory);
    }

    public static MBCategoryService getService() {
        return _service;
    }

    public void setService(MBCategoryService service) {
        _service = service;
    }
}
