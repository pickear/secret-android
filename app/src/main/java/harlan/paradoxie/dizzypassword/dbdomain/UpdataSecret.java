package harlan.paradoxie.dizzypassword.dbdomain;

import java.util.List;

/**
 * Created by a1 on 2017/12/5.
 */
public class UpdataSecret {
    /**
     * id : 17
     * secrets : [{"id":17,"name":"登录密码","value":"RtwweAo0pl8=\n","subjectId":17}]
     * title : tggg
     * url : ggvvvb
     * userId : 22
     */
    private Long sid;
    private Long id;
    private String title;
    private String url;
    private int userId;
    private List<SecretsBean> secrets;

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }
    //  private List<SecretList> secretLists;

   /* public List<SecretList> getSecretLists() {
        return secretLists;
    }

    public void setSecretLists(List<SecretList> secretLists) {
        this.secretLists = secretLists;
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<SecretsBean> getSecrets() {
        return secrets;
    }

    public void setSecrets(List<SecretsBean> secrets) {
        this.secrets = secrets;
    }

    public static class SecretsBean {
        /**
         * id : 17
         * name : 登录密码
         * value : RtwweAo0pl8=

         * subjectId : 17
         */
        private Long sid;

        public Long getSid() {
            return sid;
        }

        public void setSid(Long sid) {
            this.sid = sid;
        }
        private Long subjectId;
        private Long id;
        private String name;
        private String value;

        private long secretId;//此为 外键,和customer 对应起来

        public long getSecretId() {
            return secretId;
        }

        public void setSecretId(long secretId) {
            this.secretId = secretId;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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
