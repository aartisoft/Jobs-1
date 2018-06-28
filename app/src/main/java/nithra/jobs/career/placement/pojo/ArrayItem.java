package nithra.jobs.career.placement.pojo;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nithra-apps on 5/12/17.
 */

public class ArrayItem {
    int id;
    String item;
    boolean selected;
    List<Item> list;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public List<Item> getList() {
        return list;
    }

    public void setList(List<Item> list) {
        this.list = list;
    }
}
