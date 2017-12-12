package cf.paradoxie.dizzypassword.domian;

import java.util.List;

/**
 * Created by a1 on 2017/12/8.
 */
public class Subject {
    /**
     * id : 17
     * userId : 22
     * title : tggg
     * url : ggvvvb
     * createTime : 1512677856000
     * updateTime : 1512677906000
     * deleted : false
     * secrets : [{"id":17,"subjectId":17,"name":"登录密码","value":"hKLuNFIikOM=\n"}]
     */

    private Long id;
    private Long userId;
    private String title;
    private String url;
    private long createTime;
    private long updateTime;
    private boolean deleted;
    private List<SecretsBean> secrets;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
         * subjectId : 17
         * name : 登录密码
         * value : hKLuNFIikOM=

         */

        private Long id;
        private Long subjectId;
        private String name;
        private String value;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(Long subjectId) {
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
