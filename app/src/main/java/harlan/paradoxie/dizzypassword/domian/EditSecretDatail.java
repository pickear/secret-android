package harlan.paradoxie.dizzypassword.domian;

import harlan.paradoxie.dizzypassword.dbdomain.Secret;

/**
 * Created by Dylan on 2019/2/9.
 */

public class EditSecretDatail {
    private String code;
    private String message;
    private Secret body;

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

    public Secret getBody() {
        return body;
    }

    public void setBody(Secret body) {
        this.body = body;
    }
}
