package external.letiuka.mvc.model.dto;

import java.io.Serializable;

public class LongPaginationDTO implements Serializable {
    long targetPage;
    long lastPage;
    long perPage;

    public LongPaginationDTO() {
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
