package harlan.paradoxie.dizzypassword.appupdate.bean;

/**
 * Created by Dylan on 2019/2/6.
 */

public class UpdateInfo {
   private boolean hasUpdate;
   private int size;
   private String url;
   private String md5;
   private boolean force;
   private String description;
   private int currentVesion;

    public boolean isHasUpdate() {
        return hasUpdate;
    }

    public void setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCurrentVesion() {
        return currentVesion;
    }

    public void setCurrentVesion(int currentVesion) {
        this.currentVesion = currentVesion;
    }
}
