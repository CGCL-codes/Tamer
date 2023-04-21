package ${techspec.packageName}.entity;

import ${techspec.packageName}.entity.UploadedFile;
import java.util.List;

/**
 * ${entity.description}
 */
public interface Generated${entity.name} {
#foreach($property in $entity.properties)
#set($propertyContext = $context.get($property))

    /** 
     * Get ${property.title}.
     *  
     * @return the ${property.id}
     */
    public $propertyContext.javaType get${property.name}();

    /**
     * Set $!property.title
     *
     * @param ${property.id}
     */
    public void set${property.name}($propertyContext.javaType ${property.id});
    
#end

    public Object primaryKey();
}