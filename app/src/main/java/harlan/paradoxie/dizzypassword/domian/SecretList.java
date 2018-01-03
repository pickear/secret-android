package harlan.paradoxie.dizzypassword.domian;

import java.util.List;

/**
 * Created by a1 on 2017/11/27.
 */
public class SecretList {
    /**
     * email : string
     * id : 0
     * password : string
     * subjects : [{"id":0,"secrets":[{"id":0,"name":"string","subjectId":0,"value":"string"}],"title":"string","url":"string","userId":0}]
     * username : string
     */

    private String email;
    private Long id;
    private String password;
    private String username;
    private List<SubjectsBean> subjects;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<SubjectsBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectsBean> subjects) {
        this.subjects = subjects;
    }

    public static class SubjectsBean {
        /**
         * id : 0
         * secrets : [{"id":0,"name":"string","subjectId":0,"value":"string"}]
         * title : string
         * url : string
         * userId : 0
         */
        private Long sid;
        private Long id;
        private String title;
        private String url;
        private int userId;
        private Long createTime;
        private Long updateTime;
        private boolean deleted;
        private String account;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        private List<SecretsBean> secrets;

        public Long getSid() {
            return sid;
        }

        public void setSid(Long sid) {
            this.sid = sid;
        }

        public Long getId() {
            return id;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public Long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
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
             * id : 0
             * name : string
             * subjectId : 0
             * value : string
             */

            private Long id;
            private String name;
            private int subjectId;
            private String value;
            private long secretId;

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

            public int getSubjectId() {
                return subjectId;
            }

            public void setSubjectId(int subjectId) {
                this.subjectId = subjectId;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
