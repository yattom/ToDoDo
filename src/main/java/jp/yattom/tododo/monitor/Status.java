package jp.yattom.tododo.monitor;

public class Status {
    private final long id;
    private final boolean alive;

    public Status(long id, boolean alive) {
        this.id = id;
        this.alive = alive;
    }

    public long getId() {
        return id;
    }

    public boolean isAlive() {
        return alive;
    }
}
