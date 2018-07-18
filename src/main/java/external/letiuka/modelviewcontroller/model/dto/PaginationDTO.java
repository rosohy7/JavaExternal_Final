package external.letiuka.modelviewcontroller.model.dto;

import java.io.Serializable;

public class PaginationDTO implements Serializable {
    long targetPage;
    long lastPage;
    long perPage;

    public PaginationDTO() {
    }

    public long getTargetPage() {
        return targetPage;
    }

    public void setTargetPage(long targetPage) {
        this.targetPage = targetPage;
    }

    public long getLastPage() {
        return lastPage;
    }

    public void setLastPage(long lastPage) {
        this.lastPage = lastPage;
    }

    public long getPerPage() {
        return perPage;
    }

    public void setPerPage(long perPage) {
        this.perPage = perPage;
    }
}
