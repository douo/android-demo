package info.dourok.demo.algo;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LruCache<K, V> implements Map<K, V> {
    public int capacity;
    private LinkedHashMap<K, V> map;

    public LruCache(int capacity) {
        map = new LinkedHashMap<>(capacity * 4 / 3); //避免扩容
        this.capacity = 0;
    }


    public static void main(String[] args) {
        Map<String, String> maps = new LinkedHashMap<>(16, 0.75f, true);
        maps.put("11", "1");
        maps.put("22", "1");
        maps.put("33", "1");
        maps.put("44", "1");
        maps.put("34", "1");
        maps.put("54", "1");
        maps.get("44");
        maps.get("22");
        for (String s : maps.keySet()) {
            System.out.println(s);
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return map.values();
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
