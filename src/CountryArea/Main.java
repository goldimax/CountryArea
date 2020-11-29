package CountryArea;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Pos {
    double x, y;
    Pos(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

public class Main {
    static double getArea(JSONArray p) {
        List<Pos> pos = new ArrayList<>();
        for (Object o : p) {
            JSONArray arr = (JSONArray) o;
            pos.add(new Pos(arr.getDouble(0), arr.getDouble(1)));
        }
        double a = 0, b = 0;
        for (int i = 0; i < pos.size(); i++) {
            a += pos.get(i).x * pos.get((i + 1) % pos.size()).y;
            b += pos.get(i).y * pos.get((i + 1) % pos.size()).x;
        }
        return Math.abs(a - b / 2) * 9101160000.085981;
    }
    public static void main(String[] args) {
        try {
            String json = new BufferedReader(new FileReader("world.json"))
                    .lines().collect(Collectors.joining());
            JSONObject countries = JSON.parseObject(json);
            for (Object country : countries.getJSONArray("features")) {
                JSONObject object = (JSONObject) country;
                System.out.print(object.getJSONObject("properties").getString("name") + ": ");
                double ans = 0;
                if (object.getJSONObject("geometry").getString("type").equals("Polygon"))
                    ans = getArea(object.getJSONObject("geometry")
                            .getJSONArray("coordinates").getJSONArray(0));
                else {
                    for (Object pos : object.getJSONObject("geometry")
                            .getJSONArray("coordinates").getJSONArray(0))
                        ans += getArea((JSONArray) pos);
                }
                System.out.println(ans);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
