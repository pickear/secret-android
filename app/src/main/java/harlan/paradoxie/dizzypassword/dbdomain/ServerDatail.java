package harlan.paradoxie.dizzypassword.dbdomain;

import harlan.paradoxie.dizzypassword.domian.ServerSecret;

/**
 * Created by Dylan on 2019/2/9.
 */

public class ServerDatail {
    private String code;
    private String message;
    private ServerSecret body;

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

    public ServerSecret getBody() {
        return body;
    }

    public void setBody(ServerSecret body) {
        this.body = body;
    }
}
