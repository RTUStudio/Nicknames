package kr.rtustudio.nicknames.configuration;

import kr.rtustudio.configurate.model.ConfigurationPart;
import lombok.Getter;

@Getter
@SuppressWarnings({
        "unused",
        "CanBeFinal",
        "FieldCanBeLocal",
        "FieldMayBeFinal",
        "InnerClassMayBeStatic"
})
public class NameConfig extends ConfigurationPart {

    private String allowedRegex = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9_]*$";
    private int maxLength = 15;
}
