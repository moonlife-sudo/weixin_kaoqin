package com.njwuqi.rollcall.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static List<String> STR_STATE = new ArrayList<>();
    static {
        STR_STATE.add("正常");
        STR_STATE.add("迟到");
        STR_STATE.add("早退");
        STR_STATE.add("旷课");
        STR_STATE.add("旷课");
        STR_STATE.add("请假");
        STR_STATE.add("上课未打卡");
        STR_STATE.add("下课未打卡");
    }

    public static List<String> getStrState() {
        return STR_STATE;
    }

    //降序排序
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map)
    {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>()
        {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2)
            {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    //升序排序
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueAscending(Map<K, V> map)
    {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>()
        {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2)
            {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return compare;
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    /**
     * 判断日期是不是今天
     * @param date
     * @return    是返回true，不是返回false
     */
    public static boolean isNow(Date date) {
        // 默认的年月日的格式. yyyy-MM-dd
        String PATTEN_DEFAULT_YMD = "yyyy-MM-dd";
        // 当前时间
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(PATTEN_DEFAULT_YMD);
        //获取今天的日期
        String nowDay = sf.format(now);
        //对比的时间
        String day = sf.format(date);
        return day.equals(nowDay);
    }

    // 判断A B时间段是否重复 重复返回true 否则返回false
    public static boolean isEffectiveDate(Date startTime1, Date endTime1,Date startTime2, Date endTime2) {
        if (startTime1.getTime() == startTime2.getTime()
                || startTime1.getTime() == startTime2.getTime()
                || startTime1.getTime() == endTime2.getTime()
                || startTime1.getTime() == endTime2.getTime()
                || endTime1.getTime() == startTime2.getTime()
                || endTime1.getTime() == startTime2.getTime()
                || endTime1.getTime() == endTime2.getTime()
                || endTime1.getTime() == endTime2.getTime()) {
            return true;
        }

        // 不重叠：满足 A.end< B.start || A.start > B.end
        if (endTime1.before(startTime2)
            || startTime1.after(endTime2)) {
            return false;
        } else {
            return true;
        }
    }
    // 检查时间格式，格式为HH:mm
    public static boolean checkTime(String time) {
        try {
            String[] split = time.split(":");
            if (Integer.parseInt(split[0]) > 23 || split[0].length() > 2) {
                return false;
            }
            if (Integer.parseInt(split[1]) > 59 || split[1].length() > 2) {
                return false;
            }
        } catch (Exception e) {
            //NumberFormatException or NullPointerException
            return false;
        }
        return true;
    }
    public static boolean isPhoneNum(String phoneNum) {
        String regex = "^1[3456789]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.matches();
    }
    /*
    1、不能有特殊字符和数字；
    2、可以输入英文，可以有空格，可以输入英文名字中的点；
    3、可以输入汉字；
    4、中文英文不能同时出现；
    5、长度在2-20；
     */
    public static boolean validateName(String name) {
        if(name == null){
            return false;
        }
        String regex = "^([\u4e00-\u9fa5]{2,20}|[a-zA-Z\\.\\s]{2,20})$";
        return Pattern.matches(regex, name);
    }
    /*
    学号是6-20位数字
     */
    public static boolean validateUserNumber(String userNumber) {
        if(userNumber == null){
            return false;
        }
        String regex = "^[0-9]{6,20}$";
        return Pattern.matches(regex, userNumber);
    }

    /*
        角色只能是1或2 学生或老师
    */
    public static boolean validateRole(String role) {
        if(role == null){
            return false;
        }
        String regex = "^[1|2]$";
        return Pattern.matches(regex, role);
    }

    //课程名 3-20任意字符
    public static boolean validateCourseName(String courseName) {
        if(courseName == null){
            return false;
        }
        String regex = "^.{3,20}$";
        return Pattern.matches(regex, courseName);
    }
    //地址验证5-20任意字符
    public static boolean validateAddress(String address) {
        if(address == null){
            return false;
        }
        String regex = "^.{5,20}$";
        return Pattern.matches(regex, address);
    }

    public static String deleteAbsentStudent(String absentInfos, String student){
        if(absentInfos == null || "".equals(absentInfos)){
            return absentInfos;
        }
        if(student == null || "".equals(student)){
            return absentInfos;
        }
        absentInfos = absentInfos.replace(student,"");
        absentInfos = absentInfos.replace("##","");
        if(absentInfos.startsWith("#")){
            absentInfos=absentInfos.substring(1);
        }
        if(absentInfos.endsWith("#")){
            absentInfos=absentInfos.substring(0,absentInfos.lastIndexOf("#"));
        }
        return absentInfos;
    }
}
