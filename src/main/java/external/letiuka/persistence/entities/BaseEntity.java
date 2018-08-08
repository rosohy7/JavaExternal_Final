package external.letiuka.persistence.entities;

import java.io.Serializable;
/**
 * Represents any table with surrogate long primary key in a database.
 */
public abstract class BaseEntity implements Serializable {
    protected long id;

    public BaseEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
