package dev.wumie.system.event.dick;

import java.util.ArrayList;
import java.util.List;

public class NiuZiList {
    public final List<NiuZiInfo> dicks = new ArrayList<>();

    public void add(NiuZiInfo info) {
        dicks.add(info);
    }

    public void addAll(NiuZiInfo... info) {
        dicks.addAll(List.of(info));
    }

    public void addAll(NiuZiList info) {
        dicks.addAll(info.dicks);
    }

    public NiuZiInfo get(String qq) {
        return dicks.stream().filter(d -> d.qq_id.equals(qq)).findFirst().orElse(null);
    }

    public void remove(NiuZiInfo info) {
        dicks.remove(info);
    }

    public void remove(int id) {
        dicks.remove(id);
    }

    public NiuZiInfo get(int i) {
        return dicks.get(i);
    }

    public int size() {
        return dicks.size();
    }
}
