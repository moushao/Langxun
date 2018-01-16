package com.langbai.tdhd.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.langbai.tdhd.R;
import com.langbai.tdhd.bean.CardBean;
import com.langbai.tdhd.bean.JsonBean;
import com.langbai.tdhd.event.PickerListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mou on 2017/6/6.
 */

public class PickerUtils {
    private Context mContext;
    //    private TimePickerView pvCustomTime;
    private OptionsPickerView pvOptions;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private boolean isLoaded = false;
    PickerListener mPicker;

    public PickerUtils(Context mContext) {
        this.mContext = mContext;
    }

    public PickerUtils(Context mContext, PickerListener mPicker) {
        this.mPicker = mPicker;
        this.mContext = mContext;
    }

    public void buildSexPicker(final PickerListener mPicker) {
        final ArrayList<CardBean> sexItems = new ArrayList<>();
        sexItems.add(new CardBean(0, "男"));
        sexItems.add(new CardBean(1, "女"));
        buildTwoPicker(mContext, mPicker, sexItems);
    }

    public void buildWhetherPicker(final PickerListener mPicker) {
        //        final ArrayList<CardBean> sexItems = new ArrayList<>();
        //        sexItems.add(new CardBean(0, "是"));
        //        sexItems.add(new CardBean(1, "否"));
        //        buildTwoPicker(mContext, mPicker, sexItems);
    }

    public void buildBloodTypePicker(final PickerListener mPicker) {
        //        final ArrayList<CardBean> sexItems = new ArrayList<>();
        //        sexItems.add(new CardBean(0, "A"));
        //        sexItems.add(new CardBean(1, "B"));
        //        sexItems.add(new CardBean(2, "AB"));
        //        sexItems.add(new CardBean(3, "O"));
        //        sexItems.add(new CardBean(4, "其他"));
        //        buildTwoPicker(mContext, mPicker, sexItems);
    }

    //    public void buildBirthdayPicker(final PickerListener mPicker) {
    //        /**
    //         * @description
    //         *
    //         * 注意事项：https://github.com/Bigkoo/Android-PickerView
    //         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
    //         * 具体可参考demo 里面的两个自定义layout布局。
    //         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
    //         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
    //         */
    //        Calendar selectedDate = Calendar.getInstance();//系统当前时间
    //        Calendar startDate = Calendar.getInstance();
    //        startDate.set(1900, 1, 1);
    //        Calendar endDate = Calendar.getInstance();
    //        endDate.set(2099, 12, 31);
    //        //时间选择器 ，自定义布局
    //        pvCustomTime = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
    //            @Override
    //            public void onTimeSelect(Date date, View v) {//选中事件回调
    //                mPicker.pickerData(0, DataUtils.getTime(date));
    //                pvCustomTime.dismiss();
    //            }
    //        }).setDate(selectedDate).setRangDate(startDate, endDate)./*setLineSpacingMultiplier(3.6f)
    // */setContentSize(20)
//                    .isCyclic(true).setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
//
//                @Override
//                public void customLayout(View v) {
//                    final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
//                    ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
//                    tvSubmit.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            pvCustomTime.returnData();
//                        }
//                    });
//                    ivCancel.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });
//                }
//            }).setType(new boolean[]{true, true, true, false, false, false}).isCenterLabel(false).setLabel("", "", 
    // "", 
    //                        "", "", "")
    //                //是否只显示中间选中项的label文字，false则每项item全部都带有label。
    //                .setDividerColor(Color.RED).build();
    //        pvCustomTime.show();
    //    }

    public void buildAreaPicker(PickerListener mPicker) {
        this.mPicker = mPicker;
        mHandler.sendEmptyMessage(0x0001);
    }


    private static void buildTwoPicker(Context mContext, final PickerListener mPicker, final ArrayList<CardBean> 
            sexItems) {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView
                .OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mPicker.pickerData(options1, sexItems.get(options1).getCardNo());
            }
        }).setTitleText("").setContentTextSize(20).isDialog(false).setDividerColor(Color.GRAY).setCancelText("")
                .setDividerColor(Color.GRAY).setTextColorCenter(R.color.text_color) //设置选中项文字颜色
                /*.setLineSpacingMultiplier(3.6f)*/.setContentTextSize(20).build();
        pvOptions.setPicker(sexItems);//一级选择器
        pvOptions.show();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x0001:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    } else {
                        showAreaPickerAfterPrasen();
                    }
                    break;

                case 0x0002:
                    isLoaded = true;
                    showAreaPickerAfterPrasen();
                    break;
                case 0x0003:
                    break;

            }
        }
    };

    private void showAreaPickerAfterPrasen() {

        pvOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String area = options1Items.get(options1).getPickerViewText() + options2Items.get(options1).get
                        (options2) /*+ options3Items.get(options1).get(options2).get(options3)*/;
                pvOptions.dismiss();
                mPicker.pickerData(options1, area);
            }
        })

                .setTitleText("城市选择").setDividerColor(Color.BLACK).setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setLayoutRes(R.layout.pickerview_custom_opitions, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_add);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                pvOptions.returnData();
                            }
                        });
                    }
                }).setTextColorCenter(R.color.text_color).setDividerColor(R.color.transparent).build();

        //pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        //pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        ArrayList<JsonBean> jsonBean = CityParse.parseData(new GetJsonDataUtil().getJson(mContext, "province.json"));
        //用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null || jsonBean.get(i).getCityList().get(c)
                        .getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);
                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(0x0002);

    }
}
