package packagetest;

import org.vermeer1977.infrastructure.annotation.processor.resource.GenerateResourceEnum;
import org.vermeer1977.infrastructure.annotation.processor.resource.TargetResource;

/**
 *
 * @author Yamashita,Takahiro
 */
@GenerateResourceEnum(basePackageName = "BasePackage", subPackageName = "SubPackage2")
public class EnumBaseSubPackageName {

    @TargetResource
    final String resourceName = "resource.message9";

}
