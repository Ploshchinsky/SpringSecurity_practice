package ploton.SpringSecurity_JWT.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Error {
    private String name;
    private String message;
    private int status;
    private Date timeStamp;
}
