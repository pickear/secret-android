package cf.paradoxie.dizzypassword.appupdate.bean;

/**
 * Created by a1 on 2017/12/7.
 */
public class Update {

    /**
     * code : 0
     * data : {"download_url":"http://wap.apk.anzhi.com/data3/apk/201512/20/55089e385f6e9f350e6455f995ca3452_26503500.apk","force":false,"update_content":"测试更新接口","v_code":"10","v_name":"v1.0.0.16070810","v_sha1":"7db76e18ac92bb29ff0ef012abfe178a78477534","v_size":12365909}
     */

    private int code;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        if(data==null){
            data=new DataBean();
            return data;
        }else{
            return data;
        }

    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * download_url : http://wap.apk.anzhi.com/data3/apk/201512/20/55089e385f6e9f350e6455f995ca3452_26503500.apk
         * force : false
         * update_content : 测试更新接口
         * v_code : 10
         * v_name : v1.0.0.16070810
         * v_sha1 : 7db76e18ac92bb29ff0ef012abfe178a78477534
         * v_size : 12365909
         */

        private String download_url;
        private boolean force;
        private String update_content;
        private int v_code;
        private String v_name;
        private String v_sha1;
        private int v_size;

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }

        public boolean isForce() {
            return force;
        }

        public void setForce(boolean force) {
            this.force = force;
        }

        public String getUpdate_content() {
            return update_content;
        }

        public void setUpdate_content(String update_content) {
            this.update_content = update_content;
        }

        public int getV_code() {
            return v_code;
        }

        public void setV_code(int v_code) {
            this.v_code = v_code;
        }

        public String getV_name() {
            return v_name;
        }

        public void setV_name(String v_name) {
            this.v_name = v_name;
        }

        public String getV_sha1() {
            return v_sha1;
        }

        public void setV_sha1(String v_sha1) {
            this.v_sha1 = v_sha1;
        }

        public int getV_size() {
            return v_size;
        }

        public void setV_size(int v_size) {
            this.v_size = v_size;
        }
    }
}
