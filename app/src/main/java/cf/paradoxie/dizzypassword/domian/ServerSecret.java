package cf.paradoxie.dizzypassword.domian;

import java.util.List;

/**
 * Created by a1 on 2017/11/27.
 */
public class ServerSecret {
    /**
     * id : 13
     * userId : 22
     * title : 账号
     * url : 大大方方发发发
     * secrets : [{"id":13,"subjectId":0,"name":"登录密码","value":"pFxp5wgPEgaDeVFxmrGqgpoPddp1YrXjISnPwd5aQLg=\n"}]
     */

    private int id;
    private int userId;
    private String title;
    private String url;
    private List<SecretsBean> secrets;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<SecretsBean> getSecrets() {
        return secrets;
    }

    public void setSecrets(List<SecretsBean> secrets) {
        this.secrets = secrets;
    }

    public static class SecretsBean {
        /**
         * id : 13
         * subjectId : 0
         * name : 登录密码
         * value : pFxp5wgPEgaDeVFxmrGqgpoPddp1YrXjISnPwd5aQLg=

         */

        private int id;
        private int subjectId;
        private String name;
        private String value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(int subjectId) {
            this.subjectId = subjectId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
