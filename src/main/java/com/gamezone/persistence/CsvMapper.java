package com.gamezone.persistence;

import com.gamezone.model.Accessory;
import com.gamezone.model.Controller;
import java.util.ArrayList;
import java.util.List;

/** Converts one type of object to and from a CSV line. */
public interface CsvMapper<T> {
    /** @return the discriminator written in the first column */
    String typeCode();
    /** @return the columns after the discriminator, joined with commas */
    String write(T item);
    /** @param p all columns of the line, p[0] being the discriminator @return the object, or null if the line is incomplete */
    T read(String[] p);
}

abstract class AccessoryCsvMapper implements CsvMapper<Accessory> {
    protected String common(Accessory a) {
        return a.getId() + "," + a.getTitle() + "," + a.getPrice() + "," + a.getStock()
                + "," + String.join(";", a.getCompatibleConsoleIds());
    }
    protected List<String> consoles(String[] p) {
        List<String> list = new ArrayList<>();
        for (String c : p[5].split(";")) if (!c.isBlank()) list.add(c.trim());
        return list;
    }
}

class ControllerCsvMapper extends AccessoryCsvMapper {
    public String typeCode() { return "CONTROLLER"; }
    public String write(Accessory a) { return common(a) + "," + ((Controller) a).getConnectionType(); }
    public Accessory read(String[] p) {
        if (p.length < 7) return null;
        return new Controller(p[1], p[2], Double.parseDouble(p[3]), Integer.parseInt(p[4]), consoles(p), p[6]);
    }
}
// CableCsvMapper ("CABLE", 8 columns) and MemoryCsvMapper ("MEMORY", 8 columns): same shape