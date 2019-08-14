package br.net.easify.marvelapitest.Model;

public class Total {
    private int count;
    private int total;
    private int offset;
    private int limit;

    public Total(int count, int total, int offset, int limit) {
        this.count = count;
        this.total = total;
        this.offset = offset;
        this.limit = limit;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
