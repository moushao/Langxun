package com.langbai.tdhd.utils;

import com.google.gson.Gson;
import com.langbai.tdhd.bean.JsonBean;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Mou on 2017/5/25.
 */

public class CityParse {
    public static ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }
}
