package harlan.paradoxie.dizzypassword.dbdomain;

import java.util.List;

/**
 * Created by Dylan on 2019/2/9.
 */

public class SecretMain {
    private String code;
    private String message;
    private List<Secret> body;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Secret> getBody() {
        return body;
    }

    public void setBody(List<Secret> body) {
        this.body = body;
    }
}
