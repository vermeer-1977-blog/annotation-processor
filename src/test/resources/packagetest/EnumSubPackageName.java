package packagetest;

import org.vermeer1977.infrastructure.annotation.processor.resource.GenerateResourceEnum;
import org.vermeer1977.infrastructure.annotation.processor.resource.TargetResource;

/**
 *
 * @author Yamashita,Takahiro
 */
@GenerateResourceEnum(subPackageName = "SUBPACKAGE")
public class EnumSubPackageName {

    @TargetResource
    final String resourceName = "resource.message8";

}
