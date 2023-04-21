package com.faceye.core.service.security.service.iface;

import java.io.Serializable;
import java.util.List;
import com.faceye.core.service.iface.IBaseHibernateService;

/**
 * 
 * @author：宋海鹏
 * @Connection:E_mail:ecsun@sohu.com/myecsun@hotmail.com QQ:82676683
 * @Copy Right:www.faceye.com
 * @System:www.faceye.com网络支持系统
 * @Create Time:2007-9-22
 * @Package com.faceye.core.service.security.service.iface.IColumnService.java
 * @Description:栏目服务类
 */
public interface IColumnService extends IBaseHibernateService {

    /**
	 * 将栏目结构转化为树结构
	 * column 转化为标准的Map树结构
	 * @return
	 */
    public List transferColumns(List columns);

    /**
    * 取得Column的JSON格式数据
    * 传入的Columns可以是Column集合
    * 也可以是转化后的Map树形结构集合
    */
    public String columns2json(List columns);

    /**
    * 取得Colum 集合，如果columnId为空，则取得所有column，
    * 否则，取得指定columnId的直接子columns.
    * @param columnId
    * @return
    */
    public List getColumns(Serializable columnId);
}
