package packagetest;

import org.vermeer1977.infrastructure.annotation.processor.resource.GenerateResourceEnum;
import org.vermeer1977.infrastructure.annotation.processor.resource.TargetResource;

/**
 *
 * @author Yamashita,Takahiro
 */
@GenerateResourceEnum(basePackageName = "BASEPACKAGE")
public class EnumBasePackageName {

    @TargetResource
    final String resourceName = "resource.message7";

}
