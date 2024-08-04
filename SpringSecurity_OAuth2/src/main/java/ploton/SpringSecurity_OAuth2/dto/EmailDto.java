package ploton.SpringSecurity_OAuth2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailDto {
    @JsonProperty("email")
    private String email;
}
