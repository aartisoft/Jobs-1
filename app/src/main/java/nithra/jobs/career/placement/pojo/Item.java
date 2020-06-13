package nithra.jobs.career.placement.pojo;

import java.io.Serializable;

/**
 * Created by arunrk on 29/5/17.
 */

public class Item implements Serializable {

    int id;
    long size;
    String item;
    String count;

    public Item() {
    }

    public Item(int id, String item, String count) {
        this.id = id;
        this.item = item;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

}
