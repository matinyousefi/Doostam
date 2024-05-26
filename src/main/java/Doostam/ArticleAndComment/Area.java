package Doostam.ArticleAndComment;

import java.util.HashSet;

public class Area {
    private final String area;
    private static HashSet<Area> areas = new HashSet<>();

    public Area(String area) {
        this.area = area;
        areas.add(this);
    }

    public static HashSet<String> getAll() {
        HashSet<String> areas = new HashSet<>();
        for (Area area :
                Area.areas) {
            areas.add(area.toString());
        }
        return areas;
    }


    @Override
    public String toString() {
        return area;
    }
}
