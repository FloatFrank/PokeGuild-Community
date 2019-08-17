package com.piaofu.pokeguild;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tools {
    public static List<String> replaceString(List<String> stringList, HashMap<String, String> map) {
        List<String> strings = new ArrayList<>();
        stringList.forEach(item-> {
            final String[] string = {item};
            map.keySet().forEach(mItem-> {
                string[0] = string[0].replace(mItem, map.get(mItem));
            });
            strings.add(string[0]);
        });
        return strings;
    }

}
