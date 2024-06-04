package com.njwuqi.rollcall.businesscontroller;

import com.alibaba.fastjson.JSONObject;
import com.njwuqi.rollcall.dto.*;
import com.njwuqi.rollcall.entity.*;
import com.njwuqi.rollcall.service.*;
import com.njwuqi.rollcall.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class RollCallController {
    // 默认打卡距离范围10米
    private static double MAX_DISTANCE = 30;
    // 状态1 学生考勤记录上课
    private static final int STATE1_1=1;// 未打卡
    private static final int STATE1_2=2;// 正常
    private static final int STATE1_3=3;// 迟到
    // 状态2 学生考勤记录下课
    private static final int STATE2_1=1;// 未打卡
    private static final int STATE2_2=2;// 正常
    // modify状态 老师修改考勤记录
    private static final int MODIFY_STATE_1=1;// 未修改
    private static final int MODIFY_STATE_2=2;// 修改为请假
    // 最终状态 学生考勤计算出的状态
    //正常 0
    private static final int STATEFINAL_0=0;
    //迟到 1
    private static final int STATEFINAL_1=1;
    //早退 2
    private static final int STATEFINAL_2=2;
    //迟到+早退  3
    private static final int STATEFINAL_3=3;
    //旷课 4
    private static final int STATEFINAL_4=4;
    //请假 5
    private static final int STATEFINAL_5=5;
    //上课未打卡 6
    private static final int STATEFINAL_6=6;
    //下课未打卡 7
    private static final int STATEFINAL_7=7;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private OpenidService openidService;
    @Autowired
    private UserinfoService userinfoService;
    @Autowired
    private CourseinfoService courseinfoService;
    @Autowired
    private RuleinfoService ruleinfoService;
    @Autowired
    private ElectiveinfoService electiveinfoService;
    @Autowired
    private RecordinfoService recordinfoService;
    @Autowired
    private StatisticsinfoService statisticsinfoService;
    // 日志
    private static final Logger logger = LoggerFactory.getLogger(RollCallController.class);
    /**
     * 绑定信息
     * @return
     */
    @PostMapping("/bindingInfo")
    @ResponseBody
    public OperationResult bindingInfo(@RequestBody BindingInfodto bindingInfodto) {
        logger.info("--start bindingInfo---");
        logger.info("phone:"+bindingInfodto.getPhone());
        logger.info("realName:"+bindingInfodto.getRealName());
        logger.info("userNumber:"+bindingInfodto.getUserNumber());
        logger.info("role:"+bindingInfodto.getRole());
        logger.info("openid:"+bindingInfodto.getOpenid());
        if(!Utils.isPhoneNum(String.valueOf(bindingInfodto.getPhone()))){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(4);
            operationResult.setMessage("手机号不正确");
            return operationResult;
        }
        if(!Utils.validateName(bindingInfodto.getRealName())){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(5);
            operationResult.setMessage("姓名不正确");
            return operationResult;
        }
        if(!Utils.validateUserNumber(bindingInfodto.getUserNumber())){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(6);
            operationResult.setMessage("学号不正确（6-20位数字）");
            return operationResult;
        }
        if(!Utils.validateRole(String.valueOf(bindingInfodto.getRole()))){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(7);
            operationResult.setMessage("角色不正确（只能是学生或老师）");
            return operationResult;
        }
        // 根据openid查询redis缓存
        Openid openidObject = (Openid)redisUtil.get(RedisConst.OPENID+bindingInfodto.getOpenid());
        if(openidObject == null){
            // openid表中不存在该openid（可能该openid是非法的）
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(1);
            operationResult.setMessage("openid非法，请清空本地缓存再操作");
            return operationResult;
        }
        if(openidObject.getPhone() != 0){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(2);
            operationResult.setMessage("该用户信息已经绑定，不能再次绑定");
            return operationResult;
        }
        if(bindingInfodto.getPhone() == 0){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(4);
            operationResult.setMessage("绑定的手机号不能为0");
            return operationResult;
        }
        // 根据手机号查询用户信息是否存在
        Object o = redisUtil.get(RedisConst.USERINFO+bindingInfodto.getPhone());
        if(o != null){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(3);
            operationResult.setMessage("该手机号已经被绑定,绑定信息失败");
            return operationResult;
        }
        // 往用户表插入一条信息
        Userinfo userinfo = new Userinfo();
        userinfo.setPhone(bindingInfodto.getPhone());
        userinfo.setRealName(bindingInfodto.getRealName());
        userinfo.setUserNumber(bindingInfodto.getUserNumber());
        userinfo.setRole(bindingInfodto.getRole());
        userinfo.setState(0);//正常
        int result = userinfoService.insertUserinfo(userinfo);
        // 根据手机号查询用户信息
        userinfo = userinfoService.queryUserinfoByPhone(userinfo.getPhone());
        // redis中插入一条用户信息
        redisUtil.set(RedisConst.USERINFO+userinfo.getPhone(), userinfo);
        // 更新数据库中openid表信息
        openidObject.setPhone(userinfo.getPhone());
        openidService.updateOpenid(openidObject);
        // 修改redis中openid信息
        redisUtil.set(RedisConst.OPENID+bindingInfodto.getOpenid(),openidObject);
        // 返回结果
        OperationResult operationResult = new OperationResult();
        operationResult.setCode(0);
        operationResult.setMessage("绑定信息成功");
        logger.info("--end bindingInfo---");
        return operationResult;
    }
    /**
     * 查询个人信息
     * @return
     */
    @PostMapping("/queryUserinfo")
    @ResponseBody
    public QueryUserinforesult queryUserinfo(@RequestBody QueryUserinfodto queryUserinfodto) {
        logger.info("--start queryUserinfo---");
        logger.info("openid:"+queryUserinfodto.getOpenid());
        // 根据openid查询redis缓存
        Openid openidTable = (Openid)redisUtil.get(RedisConst.OPENID+queryUserinfodto.getOpenid());
        if(openidTable == null){
            // openid表中不存在该openid（可能该openid是非法的）
            QueryUserinforesult queryUserinforesult = new QueryUserinforesult();
            queryUserinforesult.setCode(1);
            queryUserinforesult.setMessage("openid非法，请清空本地缓存再操作");
            return queryUserinforesult;
        }
        if(openidTable.getPhone() == 0){
            QueryUserinforesult queryUserinforesult = new QueryUserinforesult();
            queryUserinforesult.setCode(2);
            queryUserinforesult.setMessage("该用户暂未绑定个人信息");
            return queryUserinforesult;
        }
        // 根据手机号查询用户信息
        Userinfo res = (Userinfo)redisUtil.get(RedisConst.USERINFO+openidTable.getPhone());
        QueryUserinforesult queryUserinforesult = new QueryUserinforesult();
        queryUserinforesult.setCode(0);
        queryUserinforesult.setMessage("查询个人信息成功");
        queryUserinforesult.setUserinfo(res);
        logger.info("--end queryUserinfo---");
        return queryUserinforesult;
    }

    /**
     * 老师创建班级（不设定打卡时间）
     * @return
     */
    @PostMapping("/createCourse")
    @ResponseBody
    public CreateCourseResult createCourse(@RequestBody CreateCoursedto createCoursedto) {
        logger.info("--start createCourse---");
        logger.info("courseName:"+createCoursedto.getCourseName());
        logger.info("addressName:"+createCoursedto.getAddressName());
        logger.info("openid:"+createCoursedto.getOpenid());
        if(!Utils.validateCourseName(createCoursedto.getCourseName())){
            CreateCourseResult createCourseResult = new CreateCourseResult();
            createCourseResult.setCode(1);
            createCourseResult.setMessage("课程名不正确（3-20任意字符）");
            return createCourseResult;
        }
        if(!Utils.validateAddress(createCoursedto.getAddressName())){
            CreateCourseResult createCourseResult = new CreateCourseResult();
            createCourseResult.setCode(2);
            createCourseResult.setMessage("地址不正确（5-20任意字符）");
            return createCourseResult;
        }
        // 根据openid查询redis缓存
        Openid openidTable = (Openid)redisUtil.get(RedisConst.OPENID+createCoursedto.getOpenid());
        if(openidTable == null){
            // openid表中不存在该openid（可能该openid是非法的）
            CreateCourseResult createCourseResult = new CreateCourseResult();
            createCourseResult.setCode(3);
            createCourseResult.setMessage("openid非法，请清空本地缓存再操作");
            return createCourseResult;
        }
        if(openidTable.getPhone() == 0){
            CreateCourseResult createCourseResult = new CreateCourseResult();
            createCourseResult.setCode(4);
            createCourseResult.setMessage("该用户暂未绑定个人信息");
            return createCourseResult;
        }
        Courseinfo courseinfo = new Courseinfo();
        courseinfo.setCourseName(createCoursedto.getCourseName());
        courseinfo.setAddressName(createCoursedto.getAddressName());
        // 获取手机号
        courseinfo.setPhone(openidTable.getPhone());
        // 00-正常
        courseinfo.setState(0);
        // 往班级表插入一条信息
        int result = courseinfoService.insertCourseinfo(courseinfo);
        // 根据手机号班级信息
        courseinfo = courseinfoService.queryCourseinfoById(courseinfo.getCourseNumber());
        // redis中插入一条班级信息
        redisUtil.sSet(RedisConst.COURSEINFOS+":"+courseinfo.getPhone(), courseinfo);
        redisUtil.sSet(RedisConst.COURSEINFOS, courseinfo);
        redisUtil.set(RedisConst.COURSEINFO+courseinfo.getCourseNumber(), courseinfo);
        // 返回结果
        CreateCourseResult createCourseResult = new CreateCourseResult();
        createCourseResult.setCode(0);
        createCourseResult.setCourseNumber(courseinfo.getCourseNumber());
        createCourseResult.setMessage("创建班级成功");
        logger.info("--end createCourse---");
        return createCourseResult;
    }

    /**
     * 根据班级号查询班级信息
     * @return
     */
    @PostMapping("/queryCourseInfoByid")
    @ResponseBody
    public QueryCourseInfoByidresult queryCourseInfoByid(@RequestBody QueryCourseInfoByiddto queryCourseInfoByiddto) {
        logger.info("--start queryCourseInfoByid---");
        logger.info("courseNumber:"+queryCourseInfoByiddto.getCourseNumber());
        // 根据班级号查询班级信息
        Courseinfo res = (Courseinfo)redisUtil.get(RedisConst.COURSEINFO+queryCourseInfoByiddto.getCourseNumber());
        if(res == null){
            QueryCourseInfoByidresult result = new QueryCourseInfoByidresult();
            result.setCode(1);
            result.setMessage("该班级不存在");
            return result;
        }
        // 根据班级号查询班级规则
        Ruleinfo ruleinfo = getNowRuleinfo(res.getCourseNumber());
        res.setRuleinfo(ruleinfo);
        // 设置老师的姓名供前端显示
        // 根据手机号查询用户信息
        Userinfo userinfo = (Userinfo)redisUtil.get(RedisConst.USERINFO+res.getPhone());
        res.setRealName(userinfo.getRealName());

        QueryCourseInfoByidresult result = new QueryCourseInfoByidresult();
        result.setCode(0);
        result.setMessage("查询班级成功");
        result.setCourseinfo(res);
        logger.info("--end queryCourseInfoByid---");
        return result;
    }

    /**
     * 判断班级是否已经关闭
     * @return
     */
    @PostMapping("/isCloseAddStudentByid")
    @ResponseBody
    public OperationResult isCloseAddStudentByid(@RequestBody IsCloseAddStudentByiddto isCloseAddStudentByiddto) {
        logger.info("--start isCloseAddStudentByid---");
        logger.info("courseNumber:"+isCloseAddStudentByiddto.getCourseNumber());
        // 返回结果
        OperationResult operationResult = new OperationResult();
        // 根据班级号查询班级信息
        Courseinfo res = (Courseinfo)redisUtil.get(RedisConst.COURSEINFO+isCloseAddStudentByiddto.getCourseNumber());
        if(res == null){
            operationResult.setCode(2);
            operationResult.setMessage("该班级不存在");
            return operationResult;
        }

        // 00-正常，01-锁定不能操作，10或11-关闭学生添加功能
        if((res.getState()>>>1)==1){
            operationResult.setCode(0);
            operationResult.setMessage("该班级已经关闭学生添加功能");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("该班级未关闭学生添加功能");
        }

        logger.info("--end isCloseAddStudentByid---");
        return operationResult;
    }

    /**
     * 老师根据班级号关闭学生添加到自己班级功能
     * @return
     */
    @PostMapping("/closeAddStudentByid")
    @ResponseBody
    public OperationResult closeAddStudentByid(@RequestBody CloseAddStudentByiddto closeAddStudentByiddto) {
        logger.info("--start closeAddStudentByid---");
        logger.info("courseNumber:"+closeAddStudentByiddto.getCourseNumber());
        // 根据班级号查询班级信息
        Courseinfo res = (Courseinfo)redisUtil.get(RedisConst.COURSEINFO+closeAddStudentByiddto.getCourseNumber());
        if(res == null){
            OperationResult result = new OperationResult();
            result.setCode(1);
            result.setMessage("该班级不存在");
            return result;
        }
        if((res.getState()>>>1)==1){
            OperationResult result = new OperationResult();
            result.setCode(2);
            result.setMessage("该班级已经关闭学生添加功能");
            return result;
        }
        // 00-正常，01-锁定不能操作，10或11-关闭学生添加功能
        res.setState(res.getState()+2);
        // redis中更新一条班级信息
        // 单个班级 key是班级号
        redisUtil.set(RedisConst.COURSEINFO+res.getCourseNumber(), res);
        // 所有班级 key是courseInfos
        deleteRedisByCourseNumber(res.getCourseNumber());
        redisUtil.sSet(RedisConst.COURSEINFOS, res);
        UpdateRedisByPhoneCourseNumber(res);
        // 修改数据库
        int result = courseinfoService.updateCourseinfo(res);
        // 返回结果
        OperationResult operationResult = new OperationResult();
        operationResult.setCode(0);
        operationResult.setMessage("关闭学生加入该班级成功");
        logger.info("--end closeAddStudentByid---");
        return operationResult;
    }

    /**
     * 老师根据班级号打开学生添加到自己班级功能
     * @return
     */
    @PostMapping("/openAddStudentByid")
    @ResponseBody
    public OperationResult openAddStudentByid(@RequestBody OpenAddStudentByiddto openAddStudentByiddto) {
        logger.info("--start openAddStudentByid---");
        logger.info("courseNumber:"+openAddStudentByiddto.getCourseNumber());
        // 根据班级号查询班级信息
        Courseinfo res = (Courseinfo)redisUtil.get(RedisConst.COURSEINFO+openAddStudentByiddto.getCourseNumber());
        if(res == null){
            OperationResult result = new OperationResult();
            result.setCode(1);
            result.setMessage("该班级不存在");
            return result;
        }
        if((res.getState()>>>1)==0){
            OperationResult result = new OperationResult();
            result.setCode(2);
            result.setMessage("该班级已经打开学生添加功能");
            return result;
        }
        // 00-正常，01-锁定不能操作，10或11-关闭学生添加功能
        if((res.getState()>>>1)==1){
            res.setState(res.getState()-2);
        }
        // redis中更新一条班级信息
        // 单个班级 key是班级号
        redisUtil.set(RedisConst.COURSEINFO+res.getCourseNumber(), res);
        // 所有班级 key是courseInfos
        deleteRedisByCourseNumber(res.getCourseNumber());
        redisUtil.sSet(RedisConst.COURSEINFOS, res);
        UpdateRedisByPhoneCourseNumber(res);
        // 修改数据库
        int result = courseinfoService.updateCourseinfo(res);
        // 返回结果
        OperationResult operationResult = new OperationResult();
        operationResult.setCode(0);
        operationResult.setMessage("打开学生加入该班级成功");
        logger.info("--end openAddStudentByid---");
        return operationResult;
    }

    /**
     * 老师查询我创建的班级
     * 学生查询我加入的班级
     * @return
     */
    @PostMapping("/queryAllCourseByPhone")
    @ResponseBody
    public QueryAllCourseByPhoneresult queryAllCourseByPhone(@RequestBody QueryAllCourseByPhonedto queryAllCourseByPhonedto) {
        logger.info("--start queryAllCourseByPhone---");
        logger.info("phone:"+queryAllCourseByPhonedto.getPhone());

        // 根据手机号查询所有班级信息
        Set<Object> courseinfoSet = redisUtil.sGet(RedisConst.COURSEINFOS+":"+queryAllCourseByPhonedto.getPhone());
        List<Courseinfo> courseinfos = new ArrayList<>();
        for (Object o:courseinfoSet) {
            Courseinfo courseinfo = (Courseinfo)o;
            // 根据班级号查询打卡规则
            Ruleinfo ruleinfo = getNowRuleinfo(courseinfo.getCourseNumber());
            courseinfo.setRuleinfo(ruleinfo);
            // 设置老师的姓名供前端显示
            // 根据手机号查询用户信息
            Userinfo userinfo = (Userinfo)redisUtil.get(RedisConst.USERINFO+courseinfo.getPhone());
            courseinfo.setRealName(userinfo.getRealName());
            courseinfos.add(courseinfo);
        }
        Collections.sort(courseinfos, new Comparator<Courseinfo>() {
            @Override
            public int compare(Courseinfo o1, Courseinfo o2) {
                return new Long(o2.getCourseNumber()-o1.getCourseNumber()).intValue();
            }
        });
        QueryAllCourseByPhoneresult result = new QueryAllCourseByPhoneresult();
        result.setCode(0);
        result.setMessage("查询成功");
        result.setCourseinfos(courseinfos);
        logger.info("--end queryAllCourseByPhone---");
        return result;
    }

    /**
     * 学生查询可以加入的班级
     * @return
     */
    @PostMapping("/queryAllCanAddCourseByPhone")
    @ResponseBody
    public QueryAllCanAddCourseByPhoneresult queryAllCanAddCourseByPhone(@RequestBody QueryAllCanAddCourseByPhoneto queryAllCanAddCourseByPhoneto) {
        logger.info("--start queryAllCanAddCourseByPhone---");
        logger.info("phone:"+queryAllCanAddCourseByPhoneto.getPhone());
        // redis 中去重复
        Set<Object> courseinfoSet = redisUtil.diff(RedisConst.COURSEINFOS,RedisConst.COURSEINFOS+":"+queryAllCanAddCourseByPhoneto.getPhone());
        List<Courseinfo> courseinfos = new ArrayList<>();
        for (Object o:courseinfoSet) {
            Courseinfo courseinfo = (Courseinfo)o;
            // 根据班级号查询打卡规则
            Ruleinfo ruleinfo = getNowRuleinfo(courseinfo.getCourseNumber());
            courseinfo.setRuleinfo(ruleinfo);
            // 设置老师的姓名供前端显示
            // 根据手机号查询用户信息
            Userinfo userinfo = (Userinfo)redisUtil.get(RedisConst.USERINFO+courseinfo.getPhone());
            courseinfo.setRealName(userinfo.getRealName());
            courseinfos.add(courseinfo);
        }
        Collections.sort(courseinfos, new Comparator<Courseinfo>() {
            @Override
            public int compare(Courseinfo o1, Courseinfo o2) {
                return new Long(o2.getCourseNumber()-o1.getCourseNumber()).intValue();
            }
        });
        QueryAllCanAddCourseByPhoneresult result = new QueryAllCanAddCourseByPhoneresult();
        result.setCode(0);
        result.setMessage("查询成功");
        result.setCourseinfos(courseinfos);
        logger.info("--end queryAllCanAddCourseByPhone---");
        return result;
    }

    /**
     * 学生加入班级
     * @return
     */
    @PostMapping("/joinCourse")
    @ResponseBody
    public OperationResult joinCourse(@RequestBody JoinCoursedto joinCoursedto) {
        logger.info("--start joinCourse---");
        logger.info("phone:"+joinCoursedto.getPhone());
        logger.info("courseNumber:"+joinCoursedto.getCourseNumber());
        // 根据班级号查询班级信息
        Courseinfo res = (Courseinfo)redisUtil.get(RedisConst.COURSEINFO+joinCoursedto.getCourseNumber());
        if(res == null){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(1);
            operationResult.setMessage("该班级不存在");
            return operationResult;
        }
        if((res.getState()>>>1)==1){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(5);
            operationResult.setMessage("该班级已经关闭学生添加功能");
            return operationResult;
        }
        if(!Utils.isPhoneNum(String.valueOf(joinCoursedto.getPhone()))){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(4);
            operationResult.setMessage("手机号不正确");
            return operationResult;
        }
        // 根据手机号查询用户信息
        Userinfo userinfo = (Userinfo)redisUtil.get(RedisConst.USERINFO+joinCoursedto.getPhone());
        if(userinfo == null){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(3);
            operationResult.setMessage("该用户不存在");
            return operationResult;
        }
        Set<Object> electiveinfoSet = redisUtil.sGet(RedisConst.ELECTIVEINFOS+joinCoursedto.getCourseNumber());
        for (Object o:electiveinfoSet) {
            Electiveinfo electiveinfo = (Electiveinfo)o;
            if(joinCoursedto.getPhone() == electiveinfo.getPhone()){
                OperationResult operationResult = new OperationResult();
                operationResult.setCode(2);
                operationResult.setMessage("你已经加入班级，不能重复加入");
                return operationResult;
            }
        }
        // 班级号
        Electiveinfo electiveinfo = new Electiveinfo();
        electiveinfo.setCourseNumber(joinCoursedto.getCourseNumber());
        electiveinfo.setPhone(joinCoursedto.getPhone());
        // 数据库中插入一条学生班级信息
        int result = electiveinfoService.insertElectiveinfo(electiveinfo);
        // 添加到redis缓存中去
        Courseinfo courseinfo = courseinfoService.queryCourseinfoById(joinCoursedto.getCourseNumber());
        redisUtil.sSet(RedisConst.COURSEINFOS+":"+joinCoursedto.getPhone(), courseinfo);
        redisUtil.sSet(RedisConst.ELECTIVEINFOS+joinCoursedto.getCourseNumber(), electiveinfo);
        OperationResult operationResult = new OperationResult();
        operationResult.setCode(0);
        operationResult.setMessage("加入班级成功");
        logger.info("--end joinCourse---");
        return operationResult;
    }

    /**
     * 老师发布打卡
     * @return
     */
    @PostMapping("/teacherPostCall")
    @ResponseBody
    public TeacherPostCallresult teacherPostCall(@RequestBody TeacherPostCalldto teacherPostCalldto) throws Exception{
        logger.info("--start teacherPostCall---");
        logger.info("courseNumber:"+teacherPostCalldto.getCourseNumber());
        logger.info("startTime:"+teacherPostCalldto.getStartTime());
        logger.info("finalTime:"+teacherPostCalldto.getFinalTime());
        logger.info("latitude:"+teacherPostCalldto.getLatitude());
        logger.info("longitude:"+teacherPostCalldto.getLongitude());
        // 检查开始
        // 根据班级号查询班级信息
        Courseinfo res = (Courseinfo)redisUtil.get(RedisConst.COURSEINFO+teacherPostCalldto.getCourseNumber());
        if(res == null){
            TeacherPostCallresult teacherPostCallresult = new TeacherPostCallresult();
            teacherPostCallresult.setCode(8);
            teacherPostCallresult.setCourseNumber(teacherPostCalldto.getCourseNumber());
            teacherPostCallresult.setMessage("发布打卡失败，该班级不存在");
            return teacherPostCallresult;
        }
        if((res.getState()>>>1)==0){
            TeacherPostCallresult teacherPostCallresult = new TeacherPostCallresult();
            teacherPostCallresult.setCode(9);
            teacherPostCallresult.setCourseNumber(teacherPostCalldto.getCourseNumber());
            teacherPostCallresult.setMessage("发布打卡失败，该班级未关闭学生添加功能");
            return teacherPostCallresult;
        }
        // 每天最多发布5次打卡
        // 查询班级规则
        int count = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Set<Object> rules = redisUtil.sGet(RedisConst.RULEINFOS+":"+teacherPostCalldto.getCourseNumber());
        Iterator<Object> ruleIterator = rules.iterator();
        while (ruleIterator.hasNext()){
            Ruleinfo ruleinfo = (Ruleinfo)ruleIterator.next();
            Date start = simpleDateFormat.parse(ruleinfo.getStartTime());
            if(Utils.isNow(start)){
               count++;
            }
        }
        if(count >= 5){
            TeacherPostCallresult teacherPostCallresult = new TeacherPostCallresult();
            teacherPostCallresult.setCode(7);
            teacherPostCallresult.setCourseNumber(teacherPostCalldto.getCourseNumber());
            teacherPostCallresult.setMessage("每天最多发布5次打卡");
            return teacherPostCallresult;
        }
        // 开始时间格式不正确，格式为HH:mm
        if(!Utils.checkTime(teacherPostCalldto.getStartTime())){
            TeacherPostCallresult teacherPostCallresult = new TeacherPostCallresult();
            teacherPostCallresult.setCode(1);
            teacherPostCallresult.setCourseNumber(teacherPostCalldto.getCourseNumber());
            teacherPostCallresult.setMessage("开始时间格式不正确，格式必须为HH:mm");
            return teacherPostCallresult;
        }
        // 结束时间格式不正确，格式为HH:mm
        if(!Utils.checkTime(teacherPostCalldto.getFinalTime())){
            TeacherPostCallresult teacherPostCallresult = new TeacherPostCallresult();
            teacherPostCallresult.setCode(2);
            teacherPostCallresult.setCourseNumber(teacherPostCalldto.getCourseNumber());
            teacherPostCallresult.setMessage("结束时间格式不正确，格式必须为HH:mm");
            return teacherPostCallresult;
        }
        // 结束时间必须大于开始时间
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,Integer.parseInt(teacherPostCalldto.getStartTime().split(":")[0]));
        c.set(Calendar.MINUTE,Integer.parseInt(teacherPostCalldto.getStartTime().split(":")[1]));
        c.set(Calendar.SECOND, 0);
        Date startDate = c.getTime();
        c.set(Calendar.HOUR_OF_DAY,Integer.parseInt(teacherPostCalldto.getFinalTime().split(":")[0]));
        c.set(Calendar.MINUTE,Integer.parseInt(teacherPostCalldto.getFinalTime().split(":")[1]));
        c.set(Calendar.SECOND, 0);
        Date finalDate = c.getTime();
        if(finalDate.before(startDate)){
            TeacherPostCallresult teacherPostCallresult = new TeacherPostCallresult();
            teacherPostCallresult.setCode(3);
            teacherPostCallresult.setCourseNumber(teacherPostCalldto.getCourseNumber());
            teacherPostCallresult.setMessage("结束时间必须大于开始时间");
            return teacherPostCallresult;
        }
        // 打卡时间段间隔必须超过10分钟
        if (finalDate.getTime() - startDate.getTime() <= 10 * 60 * 1000) {
            TeacherPostCallresult teacherPostCallresult = new TeacherPostCallresult();
            teacherPostCallresult.setCode(4);
            teacherPostCallresult.setCourseNumber(teacherPostCalldto.getCourseNumber());
            teacherPostCallresult.setMessage("打卡时间段间隔必须超过10分钟");
            return teacherPostCallresult;
        }
        // 打卡的时间需要在05:00到22:00之间
        c.set(Calendar.HOUR_OF_DAY,5);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND, 0);
        Date betweenStart = c.getTime();
        c.set(Calendar.HOUR_OF_DAY,22);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND, 0);
        Date betweenEnd = c.getTime();
        if(startDate.before(betweenStart)
                || startDate.after(betweenEnd)
                || finalDate.before(betweenStart)
                || finalDate.after(betweenEnd)){
            TeacherPostCallresult teacherPostCallresult = new TeacherPostCallresult();
            teacherPostCallresult.setCode(5);
            teacherPostCallresult.setCourseNumber(teacherPostCalldto.getCourseNumber());
            teacherPostCallresult.setMessage("打卡的时间需要在05:00到22:00之间");
            return teacherPostCallresult;
        }
        // 打卡时间发布有重叠交叉
        ruleIterator = rules.iterator();
        while (ruleIterator.hasNext()){
            Ruleinfo ruleinfo = (Ruleinfo)ruleIterator.next();
            Date start = simpleDateFormat.parse(ruleinfo.getStartTime());
            Date end = simpleDateFormat.parse(ruleinfo.getFinalTime());
            if(Utils.isEffectiveDate(startDate,finalDate,start,end)){
                TeacherPostCallresult teacherPostCallresult = new TeacherPostCallresult();
                teacherPostCallresult.setCode(6);
                teacherPostCallresult.setCourseNumber(teacherPostCalldto.getCourseNumber());
                teacherPostCallresult.setMessage("打卡时间发布有重叠交叉");
                return teacherPostCallresult;
            }
        }
        // 检查结束
        // 增加打卡规则
        Ruleinfo ruleinfo = new Ruleinfo();
        ruleinfo.setStartTime(simpleDateFormat.format(startDate));
        ruleinfo.setFinalTime(simpleDateFormat.format(finalDate));
        ruleinfo.setCourseNumber(teacherPostCalldto.getCourseNumber());
        ruleinfo.setLatitude(teacherPostCalldto.getLatitude());
        ruleinfo.setLongitude(teacherPostCalldto.getLongitude());
        ruleinfoService.insertRuleinfo(ruleinfo);
        // 添加规则到redis
        redisUtil.sSet(RedisConst.RULEINFOS+":"+ruleinfo.getCourseNumber(), ruleinfo);
        redisUtil.set(RedisConst.RULEINFO+ruleinfo.getRuleNumber(), ruleinfo);
        // 默认插入该班该时段的一条数据（一个学生）
        // 从redis中获取该班级所有学生
        Set<Object> electiveinfoSet = redisUtil.sGet(RedisConst.ELECTIVEINFOS+teacherPostCalldto.getCourseNumber());
        Iterator<Object> iterator3 = electiveinfoSet.iterator();
        while(iterator3.hasNext()){
            Object o3 = iterator3.next();
            Electiveinfo electiveinfo = (Electiveinfo)o3;
            Recordinfo recordInfo = new Recordinfo();
            recordInfo.setPhone(electiveinfo.getPhone());
            recordInfo.setCourseNumber(teacherPostCalldto.getCourseNumber());
            recordInfo.setRuleNumber(ruleinfo.getRuleNumber());
            recordInfo.setStarttime(null);
            recordInfo.setEndtime(null);
            recordInfo.setState1(STATE1_1);//未打卡
            recordInfo.setState2(STATE2_1);//未打卡
            recordInfo.setModifystate(MODIFY_STATE_1);//未修改
            recordInfo.setRulestarttime(ruleinfo.getStartTime());
            recordInfo.setRuleendtime(ruleinfo.getFinalTime());
            recordInfo.setLatitude(teacherPostCalldto.getLatitude());
            recordInfo.setLongitude(teacherPostCalldto.getLongitude());
            // todayrecordinfos:手机号:+班级号:+规则号
            redisUtil.set(RedisConst.TODAY_RECORDINFO+electiveinfo.getPhone()+":"+teacherPostCalldto.getCourseNumber()+":"+ruleinfo.getRuleNumber(), recordInfo);
        }

        TeacherPostCallresult teacherPostCallresult = new TeacherPostCallresult();
        teacherPostCallresult.setCode(0);
        teacherPostCallresult.setCourseNumber(teacherPostCalldto.getCourseNumber());
        teacherPostCallresult.setMessage("发布打卡成功");
        logger.info("--end teacherPostCall---");
        return teacherPostCallresult;
    }

    /**
     * 学生打卡
     * @return
     */
    @PostMapping("/studentRollCall")
    @ResponseBody
    public OperationResult studentRollCall(@RequestBody StudentRollCalldto studentRollCalldto) throws Exception{
        logger.info("--start studentRollCall---");
        logger.info("phone:"+studentRollCalldto.getPhone());
        logger.info("courseNumber:"+studentRollCalldto.getCourseNumber());
        logger.info("latitude:"+studentRollCalldto.getLatitude());
        logger.info("longitude:"+studentRollCalldto.getLongitude());
        // 判断是否可以打卡
        OperationResult operationResult = studentCanRollCallInner(studentRollCalldto.getPhone(),
                studentRollCalldto.getCourseNumber(),
                studentRollCalldto.getLatitude(),
                studentRollCalldto.getLongitude());
        // 如果不可以打卡返回
        if(operationResult.getCode() != 0){
            return operationResult;
        }
        // 找到最新的规则
        Ruleinfo ruleinfo = getNowRuleinfo(studentRollCalldto.getCourseNumber());
        // 从redis找到该学生的上课打卡记录(手机号+班级号+规则号 只有一条记录)
        Recordinfo recordinfo = (Recordinfo)redisUtil.get(RedisConst.TODAY_RECORDINFO+studentRollCalldto.getPhone()
                +":"+studentRollCalldto.getCourseNumber()+":"+ruleinfo.getRuleNumber());
        // 考勤开始时间 前后10分钟
        // 考勤结束时间后10分钟
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Date start = simpleDateFormat.parse(recordinfo.getRulestarttime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        cal.add(Calendar.MINUTE, -10);
        Date startqujian1 = cal.getTime(); // 上课打卡前10分钟
        cal.setTime(start);
        cal.add(Calendar.MINUTE, 10);
        Date startqujian2 = cal.getTime(); // 上课打卡后10分钟
        if (date.after(startqujian1) && date.before(startqujian2)){
            //更新starttime state1
            recordinfo.setStarttime(simpleDateFormat.format(date));
            if(date.after(startqujian1) && date.before(start) ){
                recordinfo.setState1(2);//正常考勤上课
            } else {
                recordinfo.setState1(3);//迟到
            }
        }

        Date end = simpleDateFormat.parse(recordinfo.getRuleendtime());
        cal.setTime(end);
        cal.add(Calendar.MINUTE, 10);
        Date startqujian3 = cal.getTime();//下课结束后10分钟
        if (date.after(end)  && date.before(startqujian3)){
            //更新enndtime
            recordinfo.setEndtime(simpleDateFormat.format(date));
            recordinfo.setState2(2);//下课正常考勤
        }

        redisUtil.set(RedisConst.TODAY_RECORDINFO+studentRollCalldto.getPhone()
                +":"+studentRollCalldto.getCourseNumber()+":"+ruleinfo.getRuleNumber(),recordinfo);
        operationResult.setCode(0);
        operationResult.setMessage("打卡成功");
        logger.info("--end studentRollCall---");
        return operationResult;
    }
    /**
     * 学生是否可以打卡
     * @return
     */
    @PostMapping("/studentCanRollCall")
    @ResponseBody
    public OperationResult studentCanRollCall(@RequestBody StudentCanRollCalldto studentCanRollCalldto) throws Exception{
        logger.info("--start studentCanRollCall---");
        logger.info("phone:"+studentCanRollCalldto.getPhone());
        logger.info("courseNumber:"+studentCanRollCalldto.getCourseNumber());
        logger.info("latitude:"+studentCanRollCalldto.getLatitude());
        logger.info("longitude:"+studentCanRollCalldto.getLongitude());
        OperationResult operationResult = studentCanRollCallInner(studentCanRollCalldto.getPhone(),
                studentCanRollCalldto.getCourseNumber(),
                studentCanRollCalldto.getLatitude(),
                studentCanRollCalldto.getLongitude());

        logger.info("--end studentCanRollCall---");
        return operationResult;
    }

    // 学生是否可以打卡，封装成私有函数
    // 客户端调用是否可以打卡请求、打卡都会调用
    private OperationResult studentCanRollCallInner(
            long phone,
            long courseNumber,
            double latitude,
            double longitude
    )throws Exception{
        logger.info("--start studentCanRollCallInner---");
        logger.info("phone:"+phone);
        logger.info("courseNumber:"+courseNumber);
        logger.info("latitude:"+latitude);
        logger.info("longitude:"+longitude);
        if(!Utils.isPhoneNum(String.valueOf(phone))){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(6);
            operationResult.setMessage("手机号不正确");
            return operationResult;
        }
        // 根据手机号查询用户信息
        Userinfo userinfo = (Userinfo)redisUtil.get(RedisConst.USERINFO+phone);
        if(userinfo == null){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(7);
            operationResult.setMessage("该用户不存在");
            return operationResult;
        }
        // 根据班级号查询班级信息
        Courseinfo res = (Courseinfo)redisUtil.get(RedisConst.COURSEINFO+courseNumber);
        if(res == null){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(5);
            operationResult.setMessage("该班级不存在");
            return operationResult;
        }
        // 在redis中根据手机号、班级号做为前缀，找到所有今天的考勤记录
        // 查找当前时间在starttime-10到starttime+10并且state1!=1的记录、可以打卡
        // 在可以打卡的基础上判断两个经纬度距离在10米常量之内才能打卡
        // 查找当前时间在endtime到endtime+10并且state2!=1的记录、可以打卡
        // 在可以打卡的基础上判断两个经纬度距离在15米常量之内才能打卡
        OperationResult operationResult = new OperationResult();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar cal = Calendar.getInstance();

        Set<String> recordinfoKeys = redisUtil.qianzui(RedisConst.TODAY_RECORDINFO
                +phone+":"+courseNumber+":");
        if(recordinfoKeys == null || recordinfoKeys.size() == 0){
            operationResult.setCode(1);
            operationResult.setMessage("该课程不需要打卡");
            return operationResult;
        }
//        for (String recordinfoKey:recordinfoKeys) {
//
//        }
        Iterator iterator = recordinfoKeys.iterator();
        while(iterator.hasNext()){
            String key = (String)iterator.next();
            Recordinfo recordinfo = (Recordinfo)redisUtil.get(key);
            Date start = simpleDateFormat.parse(recordinfo.getRulestarttime());
            cal.setTime(start);
            cal.add(Calendar.MINUTE, -10);
            Date startqujian1 = cal.getTime();
            cal.setTime(start);
            cal.add(Calendar.MINUTE, 10);
            Date startqujian2 = cal.getTime();
            Date end = simpleDateFormat.parse(recordinfo.getRuleendtime());
            cal.setTime(end);
            cal.add(Calendar.MINUTE, 10);
            Date startqujian3 = cal.getTime();
            boolean between = date.after(startqujian1) && date.before(startqujian3);
            boolean startCallFlg = date.after(startqujian1) && date.before(startqujian2);
            boolean endCallFlg = date.after(end) && date.before(startqujian3);
            // 在开始时间-10分钟到结束时间+10分钟
            if(between){
                // 上课下课全部打过卡
                if((recordinfo.getState1() != 1 && startCallFlg)
                    || (recordinfo.getState2() != 1 && endCallFlg)){
                    operationResult.setCode(2);
                    operationResult.setMessage("已打卡，不可以重复打卡");
                    return operationResult;
                }
                if(!startCallFlg && !endCallFlg){
                    operationResult.setCode(3);
                    operationResult.setMessage("不在考勤打卡时间内");
                    return operationResult;
                }
                double juli = new Distance(recordinfo.getLongitude(), recordinfo.getLatitude(),
                        longitude, latitude).getDistance();
                logger.info("规则经度为:"+recordinfo.getLongitude());
                logger.info("规则纬度为:"+recordinfo.getLatitude());
                logger.info("与打卡点距离为:"+juli);
                if(juli > MAX_DISTANCE){
                    operationResult.setCode(4);
                    operationResult.setMessage("不在考勤打卡范围内");
                    return operationResult;
                }
                operationResult.setCode(0);
                operationResult.setMessage("可以打卡");
                return operationResult;
            }
        }
        operationResult.setCode(3);
        operationResult.setMessage("不在考勤打卡时间内");
        logger.info("--end studentCanRollCallInner---");
        return operationResult;
    }
    /**
     * 学生查询我的某个班级的打卡信息
     * @return
     */
    @PostMapping("/studentRollCallInfo")
    @ResponseBody
    public StudentRollCallInforesult studentRollCallInfo(@RequestBody StudentRollCallInfodto studentRollCallInfodto) throws Exception{
        logger.info("--start studentRollCallInfo---");
        logger.info("phone:"+studentRollCallInfodto.getPhone());
        logger.info("courseNumber:"+studentRollCallInfodto.getCourseNumber());
        if(!Utils.isPhoneNum(String.valueOf(studentRollCallInfodto.getPhone()))){
            StudentRollCallInforesult operationResult = new StudentRollCallInforesult();
            operationResult.setCode(1);
            operationResult.setMessage("手机号不正确");
            return operationResult;
        }
        // 根据手机号查询用户信息
        Userinfo userinfo = (Userinfo)redisUtil.get(RedisConst.USERINFO+studentRollCallInfodto.getPhone());
        if(userinfo == null){
            StudentRollCallInforesult operationResult = new StudentRollCallInforesult();
            operationResult.setCode(2);
            operationResult.setMessage("该用户不存在");
            return operationResult;
        }
        // 根据班级号查询班级信息
        Courseinfo res = (Courseinfo)redisUtil.get(RedisConst.COURSEINFO+studentRollCallInfodto.getCourseNumber());
        if(res == null){
            StudentRollCallInforesult operationResult = new StudentRollCallInforesult();
            operationResult.setCode(3);
            operationResult.setMessage("该班级不存在");
            return operationResult;
        }
        List<Recordinfo> recordinfos = new ArrayList<>();
        // 在redis中根据手机号、班级号做为前缀，找到所有的非今天考勤记录
        Set<Object> infos = redisUtil.sGet(RedisConst.RECORDINFO
                +":"+studentRollCallInfodto.getPhone()+":"+studentRollCallInfodto.getCourseNumber());
        Iterator iterator = infos.iterator();
        while (iterator.hasNext()){
            Recordinfo recordinfo = (Recordinfo)iterator.next();
            recordinfos.add(recordinfo);
        }
        // 在redis中根据手机号、班级号做为前缀，找到所有的今天考勤记录
        Set<String> recordinfoKeys = redisUtil.qianzui(RedisConst.TODAY_RECORDINFO
                +studentRollCallInfodto.getPhone()+":"+studentRollCallInfodto.getCourseNumber()+":");
        if(recordinfoKeys != null){
            iterator = recordinfoKeys.iterator();
            while(iterator.hasNext()) {
                String key = (String) iterator.next();
                Recordinfo recordinfo = (Recordinfo) redisUtil.get(key);
                recordinfos.add(recordinfo);
            }
        }
        Collections.sort(recordinfos, new Comparator<Recordinfo>() {
            @Override
            public int compare(Recordinfo o1, Recordinfo o2) {
                return new Long(o2.getRuleNumber()-o1.getRuleNumber()).intValue();
            }
        });
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("HH:mm:ss");
        List<RecordinfoDisplay> recordinfoDisplays = new ArrayList<>();
        for (Recordinfo r:recordinfos) {
            RecordinfoDisplay display = new RecordinfoDisplay();
            display.setDate(simpleDateFormat2.format(simpleDateFormat1.parse(r.getRulestarttime())));
            display.setRule(simpleDateFormat3.format(simpleDateFormat1.parse(r.getRulestarttime()))
                    + "-" + simpleDateFormat3.format(simpleDateFormat1.parse(r.getRuleendtime())));
            if(r.getStarttime() == null){
                display.setStarttime("--");
            } else {
                display.setStarttime(simpleDateFormat4.format(simpleDateFormat1.parse(r.getStarttime())));
            }

            if(r.getEndtime() == null){
                display.setEndtime("--");
            } else {
                display.setEndtime(simpleDateFormat4.format(simpleDateFormat1.parse(r.getEndtime())));
            }
            display.setState(Utils.getStrState().get(getState(r)));
            recordinfoDisplays.add(display);
        }
        StudentRollCallInforesult result = new StudentRollCallInforesult();
        result.setCode(0);
        result.setMessage("查询成功");
        result.setRecordinfoDisplays(recordinfoDisplays);
        logger.info("--end studentRollCallInfo---");
        return result;
    }

    /**
     * 老师查询我的某个班级的打卡统计
     * @return
     */
    @PostMapping("/teacherRollCallInfo")
    @ResponseBody
    public TeacherRollCallInforesult teacherRollCallInfo(@RequestBody TeacherRollCallInfodto teacherRollCallInfodto) throws Exception{
        logger.info("--start teacherRollCallInfo---");
        logger.info("courseNumber:"+teacherRollCallInfodto.getCourseNumber());
        // 根据班级号查询班级信息
        Courseinfo courseinfo = (Courseinfo)redisUtil.get(RedisConst.COURSEINFO+teacherRollCallInfodto.getCourseNumber());
        if(courseinfo == null){
            TeacherRollCallInforesult operationResult = new TeacherRollCallInforesult();
            operationResult.setCode(1);
            operationResult.setMessage("该班级不存在");
            return operationResult;
        }
        List<Statisticsinfo> statisticsinfos = statistic(teacherRollCallInfodto.getCourseNumber());
        List<StatisticsinfoDisplay> statisticsinfoDisplays = new ArrayList<>();
        List<StatisticsinfoNowDisplay> statisticsinfoNowDisplays = new ArrayList<>();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        for (Statisticsinfo s: statisticsinfos) {
            Ruleinfo ruleinfo = (Ruleinfo)redisUtil.get(RedisConst.RULEINFO+s.getRuleNumber());
            Date start = simpleDateFormat1.parse(ruleinfo.getStartTime());
            cal.setTime(start);
            cal.add(Calendar.MINUTE, -10);
            Date startqujian1 = cal.getTime();
            Date end = simpleDateFormat1.parse(ruleinfo.getFinalTime());
            cal.setTime(end);
            cal.add(Calendar.MINUTE, 10);
            Date startqujian3 = cal.getTime();
            boolean between = date.after(startqujian1) && date.before(startqujian3);
            if(between){
                // 判断是否正好是打卡时间段
                StatisticsinfoNowDisplay nowdisplay = new StatisticsinfoNowDisplay();
                nowdisplay.setDate(s.getDate());
                nowdisplay.setRule(simpleDateFormat3.format(simpleDateFormat1.parse(ruleinfo.getStartTime()))
                        + "-" + simpleDateFormat3.format(simpleDateFormat1.parse(ruleinfo.getFinalTime())));
                nowdisplay.setTotalNumber(s.getTotalNumber());
                nowdisplay.setAttendanceNumber(s.getAttendanceNumber());
                nowdisplay.setLeaveNumber(s.getLeaveNumber());
                nowdisplay.setLateNumber(s.getLateNumber());
                nowdisplay.setAbsentNumber(s.getAbsentNumber());
                nowdisplay.setEarlyNumber(s.getEarlyNumber());
                nowdisplay.setEarlyInfos(s.getEarlyInfos());
                nowdisplay.setLeaveInfos(s.getLeaveInfos());
                nowdisplay.setLateInfos(s.getLateInfos());
                nowdisplay.setAbsentInfos(s.getAbsentInfos());
                nowdisplay.setStartNotCallNumber(s.getStartNotCallNumber());
                nowdisplay.setEndNotCallNumber(s.getEndNotCallNumber());
                statisticsinfoNowDisplays.add(nowdisplay);
            } else {
                // 判断是否正好是打卡时间段
                StatisticsinfoDisplay display = new StatisticsinfoDisplay();
                display.setDate(s.getDate());
                display.setRule(simpleDateFormat3.format(simpleDateFormat1.parse(ruleinfo.getStartTime()))
                        + "-" + simpleDateFormat3.format(simpleDateFormat1.parse(ruleinfo.getFinalTime())));
                display.setTotalNumber(s.getTotalNumber());
                display.setAttendanceNumber(s.getAttendanceNumber());
                display.setLeaveNumber(s.getLeaveNumber());
                display.setLateNumber(s.getLateNumber());
                display.setAbsentNumber(s.getAbsentNumber());
                display.setEarlyNumber(s.getEarlyNumber());
                display.setEarlyInfos(s.getEarlyInfos());
                display.setLeaveInfos(s.getLeaveInfos());
                display.setLateInfos(s.getLateInfos());
                display.setAbsentInfos(s.getAbsentInfos());
                statisticsinfoDisplays.add(display);
            }

        }

        TeacherRollCallInforesult res = new TeacherRollCallInforesult();
        res.setCode(0);
        res.setMessage("查询成功");
        res.setStatisticsinfoNowDisplays(statisticsinfoNowDisplays);
        res.setStatisticsinfoDisplays(statisticsinfoDisplays);
        logger.info("--end teacherRollCallInfo---");
        return res;
    }
    /**
     * 老师查询我的某个班级的某个规则的打卡统计
     * @return
     */
    @PostMapping("/teacherRollCallInfoByRuleNumber")
    @ResponseBody
    public TeacherRollCallInfoByRuleNumberresult teacherRollCallInfoByRuleNumber(@RequestBody TeacherRollCallInfoByRuleNumberdto teacherRollCallInfoByRuleNumberdto) throws Exception{
        logger.info("--start teacherRollCallInfoByRuleNumber---");
        logger.info("courseNumber:"+teacherRollCallInfoByRuleNumberdto.getCourseNumber());
        logger.info("ruleNumber:"+teacherRollCallInfoByRuleNumberdto.getRuleNumber());
        // 根据班级号查询班级信息
        Courseinfo courseinfo = (Courseinfo)redisUtil.get(RedisConst.COURSEINFO+teacherRollCallInfoByRuleNumberdto.getCourseNumber());
        if(courseinfo == null){
            TeacherRollCallInfoByRuleNumberresult operationResult = new TeacherRollCallInfoByRuleNumberresult();
            operationResult.setCode(1);
            operationResult.setMessage("该班级不存在");
            return operationResult;
        }
        List<Statisticsinfo> statisticsinfos = statistic(teacherRollCallInfoByRuleNumberdto.getCourseNumber());
        List<StatisticsinfoDisplay> statisticsinfoDisplays = new ArrayList<>();
        List<StatisticsinfoNowDisplay> statisticsinfoNowDisplays = new ArrayList<>();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        for (Statisticsinfo s: statisticsinfos) {
            if(s.getRuleNumber() != teacherRollCallInfoByRuleNumberdto.getRuleNumber()){
                continue;
            }
            Ruleinfo ruleinfo = (Ruleinfo)redisUtil.get(RedisConst.RULEINFO+s.getRuleNumber());
            Date start = simpleDateFormat1.parse(ruleinfo.getStartTime());
            cal.setTime(start);
            cal.add(Calendar.MINUTE, -10);
            Date startqujian1 = cal.getTime();
            Date end = simpleDateFormat1.parse(ruleinfo.getFinalTime());
            cal.setTime(end);
            cal.add(Calendar.MINUTE, 10);
            Date startqujian3 = cal.getTime();
            boolean between = date.after(startqujian1) && date.before(startqujian3);
            if(between){
                // 判断是否正好是打卡时间段
                StatisticsinfoNowDisplay nowdisplay = new StatisticsinfoNowDisplay();
                nowdisplay.setDate(s.getDate());
                nowdisplay.setRule(simpleDateFormat3.format(simpleDateFormat1.parse(ruleinfo.getStartTime()))
                        + "-" + simpleDateFormat3.format(simpleDateFormat1.parse(ruleinfo.getFinalTime())));
                nowdisplay.setTotalNumber(s.getTotalNumber());
                nowdisplay.setAttendanceNumber(s.getAttendanceNumber());
                nowdisplay.setLeaveNumber(s.getLeaveNumber());
                nowdisplay.setLateNumber(s.getLateNumber());
                nowdisplay.setAbsentNumber(s.getAbsentNumber());
                nowdisplay.setEarlyNumber(s.getEarlyNumber());
                nowdisplay.setEarlyInfos(s.getEarlyInfos());
                nowdisplay.setLeaveInfos(s.getLeaveInfos());
                nowdisplay.setLateInfos(s.getLateInfos());
                nowdisplay.setAbsentInfos(s.getAbsentInfos());
                nowdisplay.setStartNotCallNumber(s.getStartNotCallNumber());
                nowdisplay.setEndNotCallNumber(s.getEndNotCallNumber());
                statisticsinfoNowDisplays.add(nowdisplay);
            } else {
                // 判断是否正好是打卡时间段
                StatisticsinfoDisplay display = new StatisticsinfoDisplay();
                display.setDate(s.getDate());
                display.setRule(simpleDateFormat3.format(simpleDateFormat1.parse(ruleinfo.getStartTime()))
                        + "-" + simpleDateFormat3.format(simpleDateFormat1.parse(ruleinfo.getFinalTime())));
                display.setTotalNumber(s.getTotalNumber());
                display.setAttendanceNumber(s.getAttendanceNumber());
                display.setLeaveNumber(s.getLeaveNumber());
                display.setLateNumber(s.getLateNumber());
                display.setAbsentNumber(s.getAbsentNumber());
                display.setEarlyNumber(s.getEarlyNumber());
                display.setEarlyInfos(s.getEarlyInfos());
                display.setLeaveInfos(s.getLeaveInfos());
                display.setLateInfos(s.getLateInfos());
                display.setAbsentInfos(s.getAbsentInfos());
                statisticsinfoDisplays.add(display);
            }

        }

        TeacherRollCallInfoByRuleNumberresult res = new TeacherRollCallInfoByRuleNumberresult();
        res.setCode(0);
        res.setMessage("查询成功");
        res.setStatisticsinfoNowDisplays(statisticsinfoNowDisplays);
        res.setStatisticsinfoDisplays(statisticsinfoDisplays);
        logger.info("--end teacherRollCallInfoByRuleNumber---");
        return res;
    }
    /**
     * 老师查询我的某个班级上课以来的统计
     * @return
     */
    @PostMapping("/teacherRollALLCallInfo")
    @ResponseBody
    public TeacherRollALLCallInforesult teacherRollALLCallInfo(@RequestBody TeacherRollALLCallInfodto teacherRollALLCallInfodto) throws Exception{
        logger.info("--start teacherRollALLCallInfo---");
        logger.info("courseNumber:"+teacherRollALLCallInfodto.getCourseNumber());
        Statisticsinfos statisticsinfos = new Statisticsinfos();
        Courseinfo courseinfo = (Courseinfo)redisUtil.get(RedisConst.COURSEINFO+teacherRollALLCallInfodto.getCourseNumber());
        if(courseinfo == null){
            TeacherRollALLCallInforesult result = new TeacherRollALLCallInforesult();
            result.setCode(1);
            result.setMessage("该班级不存在");
            return result;
        }
        // 根据手机号查询用户信息
        Userinfo userinfo = (Userinfo)redisUtil.get(RedisConst.USERINFO+courseinfo.getPhone());
        courseinfo.setRealName(userinfo.getRealName());
        statisticsinfos.setCourseinfo(courseinfo);

        String earlyInfos = "";
        String leaveInfos = "";
        String lateInfos = "";
        String absentInfos = "";
        List<Statisticsinfo> statisticsinfoList = statistic(courseinfo.getCourseNumber());
        Iterator<Statisticsinfo> iterator = statisticsinfoList.iterator();
        while(iterator.hasNext()){
            Statisticsinfo statisticsinfo = iterator.next();
            earlyInfos+="#"+statisticsinfo.getEarlyInfos();
            leaveInfos+="#"+statisticsinfo.getLeaveInfos();
            lateInfos+="#"+statisticsinfo.getLateInfos();
            absentInfos+="#"+statisticsinfo.getAbsentInfos();
        }
        // 20220113张三#20220114张三#20220113张三#20220114张四#
        // key 学号姓名 value是出现次数
        statisticsinfos.setAbsentInfos(analysis(absentInfos));
        statisticsinfos.setLateInfos(analysis(lateInfos));
        statisticsinfos.setEarlyInfos(analysis(earlyInfos));
        statisticsinfos.setLeaveInfos(analysis(leaveInfos));
        TeacherRollALLCallInforesult result = new TeacherRollALLCallInforesult();
        result.setCode(0);
        result.setMessage("查询成功");
        result.setStatisticsinfos(statisticsinfos);
        logger.info("--end teacherRollALLCallInfo---");
        return result;
    }
    // 某个班级的打卡统计
    private List<Statisticsinfo> statistic(long courseNumber) throws Exception{
        List<Statisticsinfo> statisticsinfos = new ArrayList<>();
        // 在redis中根据手机号、班级号做为前缀，找到所有的非今天考勤记录
        Set<Object> infos = redisUtil.sGet(RedisConst.STATISTICSINFO+":"+courseNumber);
        Iterator iterator = infos.iterator();
        while (iterator.hasNext()){
            Statisticsinfo statisticsinfo = (Statisticsinfo)iterator.next();
            statisticsinfos.add(statisticsinfo);
        }
        // 查询班级规则
        Set<Object> rules = redisUtil.sGet(RedisConst.RULEINFOS+":"+courseNumber);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Iterator<Object> ruleIterator = rules.iterator();
        while (ruleIterator.hasNext()){
            Ruleinfo ruleinfo = (Ruleinfo)ruleIterator.next();
            Date start = simpleDateFormat.parse(ruleinfo.getStartTime());
            Date end = simpleDateFormat.parse(ruleinfo.getFinalTime());
            if(Utils.isNow(start) && Utils.isNow(end)){
                Statisticsinfo statistic = statistic(courseNumber, ruleinfo.getRuleNumber());
                if(statistic != null){
                    statisticsinfos.add(statistic);
                }

            }
        }
        return statisticsinfos;
    }
    // 今天某个班级中某个规则打卡统计
    public Statisticsinfo statistic(long courseNumber, long ruleNumber ) throws Exception{
        List<Recordinfo> recordinfos = new ArrayList<>();
        Statisticsinfo statisticsinfo = new Statisticsinfo();
        statisticsinfo.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        statisticsinfo.setCourseNumber(courseNumber);
        statisticsinfo.setRuleNumber(ruleNumber);
        int totalNumber = 0;
        int attendanceNumber = 0;
        int leaveNumber = 0;
        int lateNumber = 0;
        int earlyNumber = 0;
        int absentNumber = 0;
        int startNotCallNumber = 0;
        int endNotCallNumber = 0;
        String earlyInfos = "";
        String leaveInfos = "";
        String lateInfos = "";
        String absentInfos = "";
        // 查询班里所有学生
        Set<Object> electiveInfos = redisUtil.sGet(RedisConst.ELECTIVEINFOS+courseNumber);
        // 遍历所有学生的手机号
        Iterator<Object> iterator = electiveInfos.iterator();
        while (iterator.hasNext()){
            Electiveinfo electiveinfo = (Electiveinfo)iterator.next();
            long phone = electiveinfo.getPhone();
            // 在redis中根据手机号、班级号、规则做为前缀，找到所有的今天考勤记录,并计算考勤统计
            Set<String> recordinfoKeys = redisUtil.qianzui(RedisConst.TODAY_RECORDINFO+phone+":"+courseNumber+":"+ruleNumber);
            if(recordinfoKeys != null){
                Iterator<String> iterator2 = recordinfoKeys.iterator();
                while(iterator2.hasNext()) {
                    String key = iterator2.next();
                    Recordinfo recordinfo = (Recordinfo) redisUtil.get(key);
                    recordinfos.add(recordinfo);
                }
            }
        }
        // 学生生成的考勤记录不存在
        // 这种情况是手动重启服务器，已经把数据同步到数据库了，再重启或半夜同步会生成全是0的记录
        if(recordinfos.size() == 0){
            return null;
        }
        // 遍历所有recordinfo
        Iterator<Recordinfo> recordinfoIterator = recordinfos.iterator();
        while (recordinfoIterator.hasNext()){
            Recordinfo recordinfo = recordinfoIterator.next();
            // 获取学生信息
            // 根据手机号查询用户信息
            Userinfo info = (Userinfo)redisUtil.get(RedisConst.USERINFO+recordinfo.getPhone());
            int state = getState(recordinfo);
            totalNumber++;//总人数加1
            if(state == STATEFINAL_0){
                attendanceNumber++;//出勤人数
            }
            if(state == STATEFINAL_1){
                lateNumber++;//迟到人数
                lateInfos+="#"+info.getUserNumber()+info.getRealName();
            }
            if(state == STATEFINAL_2){
                earlyNumber++;//早退人数
                earlyInfos+="#"+info.getUserNumber()+info.getRealName();
            }
            if(state == STATEFINAL_3 || state == STATEFINAL_4){
                absentNumber++;//旷课人数
                absentInfos+="#"+info.getUserNumber()+info.getRealName();
            }
            if(state == STATEFINAL_5){
                leaveNumber++;//请假人数
                leaveInfos+="#"+info.getUserNumber()+info.getRealName();
            }
            if(state == STATEFINAL_6){
                startNotCallNumber++;//上课未打卡人数
            }
            if(state == STATEFINAL_7){
                endNotCallNumber++;//下课未打卡人数
            }
        }
        statisticsinfo.setTotalNumber(totalNumber);
        statisticsinfo.setAttendanceNumber(attendanceNumber);
        statisticsinfo.setLeaveNumber(leaveNumber);
        statisticsinfo.setLateNumber(lateNumber);
        statisticsinfo.setEarlyNumber(earlyNumber);
        statisticsinfo.setAbsentNumber(absentNumber);
        statisticsinfo.setStartNotCallNumber(startNotCallNumber);
        statisticsinfo.setEndNotCallNumber(endNotCallNumber);
        if(earlyInfos.startsWith("#")){
            earlyInfos = earlyInfos.substring(1);
        }
        if(leaveInfos.startsWith("#")){
            leaveInfos = leaveInfos.substring(1);
        }
        if(lateInfos.startsWith("#")){
            lateInfos = lateInfos.substring(1);
        }
        if(absentInfos.startsWith("#")){
            absentInfos = absentInfos.substring(1);
        }
        statisticsinfo.setEarlyInfos(earlyInfos);
        statisticsinfo.setLeaveInfos(leaveInfos);
        statisticsinfo.setLateInfos(lateInfos);
        statisticsinfo.setAbsentInfos(absentInfos);
        return statisticsinfo;
    }
    /**
     * 老师根据班级号查询班级所有学生
     *
     * @return
     */
    @PostMapping("/queryAllStudentByCourseid")
    @ResponseBody
    public QueryAllStudentByCourseidresult queryAllStudentByCourseid(@RequestBody QueryAllStudentByCourseiddto queryAllStudentByCourseiddto) {
        logger.info("--start queryAllStudentByCourseid---");
        logger.info("courseNumber:"+queryAllStudentByCourseiddto.getCourseNumber());
        // 根据班级号查询班级信息
        Courseinfo res = (Courseinfo)redisUtil.get(RedisConst.COURSEINFO+queryAllStudentByCourseiddto.getCourseNumber());
        if(res == null){
            QueryAllStudentByCourseidresult result = new QueryAllStudentByCourseidresult();
            result.setCode(1);
            result.setMessage("该班级不存在");
            return result;
        }
        List<Userinfo> userinfos = new ArrayList<>();
        // 从redis中获取该班级所有学生
        Set<Object> electiveinfoSet = redisUtil.sGet(RedisConst.ELECTIVEINFOS+queryAllStudentByCourseiddto.getCourseNumber());
        for (Object o:electiveinfoSet) {
            Electiveinfo electiveinfo = (Electiveinfo)o;
            Userinfo userinfo = (Userinfo)redisUtil.get(RedisConst.USERINFO+electiveinfo.getPhone());
            userinfos.add(userinfo);
        }
        // 以学号升序排序
        Collections.sort(userinfos, new Comparator<Userinfo>() {
            @Override
            public int compare(Userinfo o1, Userinfo o2) {
                return o1.getUserNumber().compareTo(o2.getUserNumber());
            }
        });
        QueryAllStudentByCourseidresult result = new QueryAllStudentByCourseidresult();
        result.setCode(0);
        result.setMessage("查询班级学生成功");
        result.setUserinfos(userinfos);
        logger.info("--end queryAllStudentByCourseid---");
        return result;
    }
    /**
     * 计算出学生考勤状态
     * @param recordinfo
     * @return
     */
    private int getState(Recordinfo recordinfo) throws Exception{
        int result = STATEFINAL_4;
        long state1 = recordinfo.getState1();
        long state2 = recordinfo.getState2();
        if(recordinfo.getModifystate() == MODIFY_STATE_2){
            result = STATEFINAL_5;
            // 老师把考勤直接改成请假了，就直接认定是请假，不需要其它判断，即使是正在打卡比如迟到
            return result;
        } else {
            if(state1 ==STATE1_1 && state2 == STATE2_1){
                result = STATEFINAL_4;
            } else if(state1 ==STATE1_1 && state2 == STATE2_2){
                result = STATEFINAL_1;
            } else if(state1 ==STATE1_2 && state2 == STATE2_1){
                result = STATEFINAL_2;
            } else if(state1 ==STATE1_2 && state2 == STATE2_2){
                result = STATEFINAL_0;
            } else if(state1 ==STATE1_3 && state2 == STATE2_1){
                result = STATEFINAL_3;
            } else if(state1 ==STATE1_3 && state2 == STATE2_2){
                result = STATEFINAL_1;
            }
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        Date start = simpleDateFormat.parse(recordinfo.getRulestarttime());
        cal.setTime(start);
        cal.add(Calendar.MINUTE, -10);
        Date startqujian1 = cal.getTime();
        cal.setTime(start);
        cal.add(Calendar.MINUTE, 10);
        Date startqujian2 = cal.getTime();
        Date end = simpleDateFormat.parse(recordinfo.getRuleendtime());
        cal.setTime(end);
        cal.add(Calendar.MINUTE, 10);
        Date startqujian3 = cal.getTime();
        boolean between = date.after(startqujian1) && date.before(startqujian3);
        boolean startCallFlg = date.after(startqujian1) && date.before(startqujian2);
        boolean endCallFlg = date.after(end) && date.before(startqujian3);
        // 正好在打卡时间段前
        if(date.before(startqujian1)){
            result = STATEFINAL_6;
            // 正好在打卡时间段内的统计
            // 开始时间前10分钟--下课时间后10分钟
        } else if(between){
            // 正好在上课打卡时间段内
            // 开始时间前10分钟
            // 正好在上课时间内，不能打卡
            if(startCallFlg || !endCallFlg){
                if(STATE1_1 == state1){
                    result = STATEFINAL_6;
                } else if(STATE1_2 == state1){
                    result = STATEFINAL_0;
                } else if(STATE1_3 == state1){
                    result = STATEFINAL_1;
                }
            } else{
                // 正好在下课打卡时间段内
                // 下课时间后10分钟
                if(STATE2_1 == state2){
                    result = STATEFINAL_7;
                }
            }
        }
        return result;
    }

    /**
     * 根据字符串解析
     * @param info 20220113张三#20220114张三#20220113张三#20220114张四#
     * @return 所有xx学生 按学生学号+姓名、xx次数 降序
     */
    private List<String> analysis(String info) {
        List<String> result = new ArrayList<>();
        if(info == null || "".equals(info)){
            return result;
        }
        Map<String, Integer> tongji = new TreeMap<>();
        String[] datas = info.split("#");
        for (int i = 0; i < datas.length; i++) {
            String data = datas[i];
            if(data != null && !"".equals(data)){
                if (tongji.containsKey(data)) {
                    tongji.put(data, tongji.get(data) + 1);
                } else {
                    tongji.put(data, 1);
                }
            }
        }
        // 降序
        tongji = Utils.sortByValueDescending(tongji);
        for (Map.Entry<String, Integer> entry : tongji.entrySet()) {
            result.add(entry.getKey() + "#" + entry.getValue());
        }
        return result;
    }

    /**
     * redis中查询所有班级，然后删除某个班级（根据班级号）
     */
    private void deleteRedisByCourseNumber(long courseNumber){
        Set<Object> os = redisUtil.sGet(RedisConst.COURSEINFOS);
        Iterator<Object> iterator = os.iterator();
        while(iterator.hasNext()){
            Courseinfo courseinfo = (Courseinfo)iterator.next();
            if(courseNumber == courseinfo.getCourseNumber()){
                redisUtil.setRemove(RedisConst.COURSEINFOS,courseinfo);
            }
        }
    }
    /**
     * redis中查询手机号对应所有班级，然后删除某个班级（根据班级号）
     */
    private void deleteRedisByPhoneCourseNumber(long phone, long courseNumber){
        Set<Object> os = redisUtil.sGet(RedisConst.COURSEINFOS+":"+phone);
        Iterator<Object> iterator = os.iterator();
        while(iterator.hasNext()){
            Courseinfo courseinfo = (Courseinfo)iterator.next();
            if(courseNumber == courseinfo.getCourseNumber()){
                redisUtil.setRemove(RedisConst.COURSEINFOS+":"+phone,courseinfo);
            }
        }
    }

    /**
     * 遍历以courseInfos:手机号为前缀的key
     * 找到班级号为courseNumber，删除并添加
     */
    private void UpdateRedisByPhoneCourseNumber(Courseinfo courseinfo){
        Set<String> keys = redisUtil.qianzui(RedisConst.COURSEINFOS+":");
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            Set<Object> os = redisUtil.sGet(key);
            Iterator<Object> iterator2 = os.iterator();
            while(iterator2.hasNext()){
                Courseinfo courseinfo2 = (Courseinfo)iterator2.next();
                if(courseinfo.getCourseNumber() == courseinfo2.getCourseNumber()){
                    redisUtil.setRemove(key,courseinfo2);
                    redisUtil.sSet(key,courseinfo);
                }
            }
        }
    }

    /**
     * 查询当前打卡规则 规则号最大的打卡规则
     * @param courseNumber
     */
    private Ruleinfo getNowRuleinfo(long courseNumber){
        long ruleNumber = -1;
        Ruleinfo ruleinfo = null;
        Ruleinfo max = null;
        Set<Object> rules = redisUtil.sGet(RedisConst.RULEINFOS+":"+courseNumber);
        Iterator<Object> iterator = rules.iterator();
        while (iterator.hasNext()) {
            ruleinfo = (Ruleinfo)iterator.next();
            if(ruleinfo.getRuleNumber() > ruleNumber){
                ruleNumber = ruleinfo.getRuleNumber();
                max = ruleinfo;
            }
        }
        return max;
    }

    /**
     * 从班级中删除学生
     * @return
     */
    @PostMapping("/deleteStudentFromCourse")
    @ResponseBody
    public OperationResult deleteStudentFromCourse(@RequestBody DeleteStudentFromCoursedto deleteStudentFromCoursedto) {
        logger.info("--start deleteStudentFromCourse---");
        logger.info("phone:"+deleteStudentFromCoursedto.getPhone());
        logger.info("courseNumber:"+deleteStudentFromCoursedto.getCourseNumber());
        // 根据班级号查询班级信息
        Courseinfo res = (Courseinfo)redisUtil.get(RedisConst.COURSEINFO+deleteStudentFromCoursedto.getCourseNumber());
        if(res == null){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(1);
            operationResult.setMessage("该班级不存在");
            return operationResult;
        }
        // 根据手机号查询用户信息
        Userinfo userinfo = (Userinfo)redisUtil.get(RedisConst.USERINFO+deleteStudentFromCoursedto.getPhone());
        if(userinfo == null){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(2);
            operationResult.setMessage("该学生不存在");
            return operationResult;
        }
        // 根据班级号和学生手机号班级表Electiveinfo
        Electiveinfo electiveinfoparam = new Electiveinfo();
        electiveinfoparam.setPhone(deleteStudentFromCoursedto.getPhone());
        electiveinfoparam.setCourseNumber(deleteStudentFromCoursedto.getCourseNumber());
        Electiveinfo electiveinfo = electiveinfoService.queryElectiveinfoByphonecourseNumber(electiveinfoparam);
        if(electiveinfo == null){
            OperationResult operationResult = new OperationResult();
            operationResult.setCode(3);
            operationResult.setMessage("班级中不存在该学生");
            return operationResult;
        }

        // 表中根据班级id删除班级中该学生
        electiveinfoService.delElectiveinfo(electiveinfo.getId());

        // 缓存中根据学生手机号删除我的班级
        // courseInfos:xxx
        // 根据手机号查询所有班级信息
        Set<Object> courseinfoSet = redisUtil.sGet(RedisConst.COURSEINFOS+":"+deleteStudentFromCoursedto.getPhone());
        for (Object o:courseinfoSet) {
            Courseinfo courseinfo = (Courseinfo) o;
            if(courseinfo.getCourseNumber() == deleteStudentFromCoursedto.getCourseNumber()){
                redisUtil.setRemove(RedisConst.COURSEINFOS+":"+deleteStudentFromCoursedto.getPhone(),courseinfo);
                break;
            }
        }
        // 根据班级号查询班级下所有学生
        // electiveInfos:xx
        Set<Object> electiveinfoSet = redisUtil.sGet(RedisConst.ELECTIVEINFOS+deleteStudentFromCoursedto.getCourseNumber());
        for (Object o:electiveinfoSet) {
            electiveinfo = (Electiveinfo)o;
            if(electiveinfo.getPhone() == deleteStudentFromCoursedto.getPhone()){
                redisUtil.setRemove(RedisConst.ELECTIVEINFOS+deleteStudentFromCoursedto.getCourseNumber(),electiveinfo);
                break;
            }
        }
        OperationResult operationResult = new OperationResult();
        operationResult.setCode(0);
        operationResult.setMessage("从班级中删除学生成功");
        logger.info("--end deleteStudentFromCourse---");
        return operationResult;
    }

    /**
     * 老师将学生考勤改为请假
     * @return
     */
    @PostMapping("/teacherUpdateStudentLeave")
    @ResponseBody
    public TeacherUpdateStudentLeaveresult teacherUpdateStudentLeave(@RequestBody TeacherUpdateStudentLeavedto teacherUpdateStudentLeavedto) throws Exception{
        logger.info("--start teacherUpdateStudentLeave---");
        logger.info("courseNumber:"+teacherUpdateStudentLeavedto.getCourseNumber());
        logger.info("phone:"+teacherUpdateStudentLeavedto.getPhone());
        logger.info("ruleNumber:"+teacherUpdateStudentLeavedto.getRuleNumber());

        if(!Utils.isPhoneNum(String.valueOf(teacherUpdateStudentLeavedto.getPhone()))){
            TeacherUpdateStudentLeaveresult operationResult = new TeacherUpdateStudentLeaveresult();
            operationResult.setCode(1);
            operationResult.setMessage("手机号不正确");
            return operationResult;
        }
        // 根据手机号查询用户信息
        Userinfo userinfo = (Userinfo)redisUtil.get(RedisConst.USERINFO+teacherUpdateStudentLeavedto.getPhone());
        if(userinfo == null){
            TeacherUpdateStudentLeaveresult operationResult = new TeacherUpdateStudentLeaveresult();
            operationResult.setCode(2);
            operationResult.setMessage("该学生不存在");
            return operationResult;
        }
        // 根据班级号查询班级信息
        Courseinfo courseinfo = (Courseinfo)redisUtil.get(RedisConst.COURSEINFO+teacherUpdateStudentLeavedto.getCourseNumber());
        if(courseinfo == null){
            TeacherUpdateStudentLeaveresult operationResult = new TeacherUpdateStudentLeaveresult();
            operationResult.setCode(3);
            operationResult.setMessage("该班级不存在");
            return operationResult;
        }
        // 根据规则号查询规则
        Ruleinfo ruleinfo = (Ruleinfo) redisUtil.get(RedisConst.RULEINFO+teacherUpdateStudentLeavedto.getRuleNumber());
        if(ruleinfo == null){
            TeacherUpdateStudentLeaveresult operationResult = new TeacherUpdateStudentLeaveresult();
            operationResult.setCode(4);
            operationResult.setMessage("该规则不存在");
            return operationResult;
        }
        // 查询该学生所有的班级，是否存在该班级
        boolean isInCourse = false;
        Set<Object> courseinfoSet = redisUtil.sGet(RedisConst.COURSEINFOS+":"+teacherUpdateStudentLeavedto.getPhone());
        for (Object o:courseinfoSet) {
            Courseinfo temp = (Courseinfo) o;
            if(temp.getCourseNumber() == teacherUpdateStudentLeavedto.getCourseNumber()){
                isInCourse = true;
                break;
            }
        }
        if(!isInCourse){
            TeacherUpdateStudentLeaveresult operationResult = new TeacherUpdateStudentLeaveresult();
            operationResult.setCode(5);
            operationResult.setMessage("该学生不在班级中");
            return operationResult;
        }
        // 判断该学生是否在本次打卡规则内
        // 今天的recordinfo中查找
        Recordinfo todayrecordinfo = (Recordinfo)redisUtil.get(RedisConst.TODAY_RECORDINFO+teacherUpdateStudentLeavedto.getPhone()
                +":"+teacherUpdateStudentLeavedto.getCourseNumber()+":"+ruleinfo.getRuleNumber());
        if(todayrecordinfo!=null){
            // 找到了直接在缓存中修改
            todayrecordinfo.setModifystate(MODIFY_STATE_2);
            redisUtil.set(RedisConst.TODAY_RECORDINFO+teacherUpdateStudentLeavedto.getPhone()+":"+teacherUpdateStudentLeavedto.getCourseNumber()+":"+ruleinfo.getRuleNumber(), todayrecordinfo);
            TeacherUpdateStudentLeaveresult operationResult = new TeacherUpdateStudentLeaveresult();
            operationResult.setCode(0);
            operationResult.setMessage("修改成功");
            logger.info("--end teacherUpdateStudentLeave---");
            return operationResult;
        }
        // 历史的recordinfo中查找
        Recordinfo historyrecordinfo = null;
        Set<Object> historyinfos = redisUtil.sGet(RedisConst.RECORDINFO
                +":"+teacherUpdateStudentLeavedto.getPhone()+":"+teacherUpdateStudentLeavedto.getCourseNumber());
        Iterator iterator = historyinfos.iterator();
        while (iterator.hasNext()){
            historyrecordinfo = (Recordinfo)iterator.next();
            if(historyrecordinfo.getRuleNumber() == teacherUpdateStudentLeavedto.getRuleNumber()){
                // 找到了修改缓存
                redisUtil.setRemove(RedisConst.RECORDINFO
                        +":"+teacherUpdateStudentLeavedto.getPhone()+":"+teacherUpdateStudentLeavedto.getCourseNumber(), historyrecordinfo);
                historyrecordinfo.setModifystate(MODIFY_STATE_2);
                redisUtil.sSet(RedisConst.RECORDINFO
                        +":"+teacherUpdateStudentLeavedto.getPhone()+":"+teacherUpdateStudentLeavedto.getCourseNumber(), historyrecordinfo);
                // 找到了修改考勤记录数据库
                recordinfoService.updateRecordinfo(historyrecordinfo);
                // 缓存找到该考勤统计
                Set<Object> statisticsinfos = redisUtil.sGet(RedisConst.STATISTICSINFO+":"+teacherUpdateStudentLeavedto.getCourseNumber());
                iterator = statisticsinfos.iterator();
                while (iterator.hasNext()){
                    Statisticsinfo statisticsinfo = (Statisticsinfo)iterator.next();
                    if(statisticsinfo.getRuleNumber() == teacherUpdateStudentLeavedto.getRuleNumber()){
                        // 缓存中删除
                        redisUtil.setRemove(RedisConst.STATISTICSINFO+":"+teacherUpdateStudentLeavedto.getCourseNumber(),statisticsinfo);
                        statisticsinfo.setAbsentNumber(statisticsinfo.getAbsentNumber()-1);
                        String absentInfos = statisticsinfo.getAbsentInfos();
                        absentInfos = Utils.deleteAbsentStudent(absentInfos,userinfo.getUserNumber()+userinfo.getRealName());
                        statisticsinfo.setAbsentInfos(absentInfos);
                        statisticsinfo.setLeaveNumber(statisticsinfo.getLeaveNumber()+1);
                        String leaveInfos = statisticsinfo.getLeaveInfos();
                        if(leaveInfos == null || "".equals(leaveInfos)){
                            leaveInfos = userinfo.getUserNumber()+userinfo.getRealName();
                        } else {
                            leaveInfos+="#"+userinfo.getUserNumber()+userinfo.getRealName();
                        }
                        statisticsinfo.setLeaveInfos(leaveInfos);
                        // 缓存中添加
                        redisUtil.sSet(RedisConst.STATISTICSINFO+":"+teacherUpdateStudentLeavedto.getCourseNumber(), statisticsinfo);
                        // 更新数据库
                        statisticsinfoService.updateStatisticsinfo(statisticsinfo);
                        TeacherUpdateStudentLeaveresult operationResult = new TeacherUpdateStudentLeaveresult();
                        operationResult.setCode(0);
                        operationResult.setMessage("修改成功");
                        logger.info("--end teacherUpdateStudentLeave---");
                        return operationResult;
                    }
                }
            }
        }
        TeacherUpdateStudentLeaveresult operationResult = new TeacherUpdateStudentLeaveresult();
        operationResult.setCode(6);
        operationResult.setMessage("该学生不在该考勤规则内");
        logger.info("--end teacherUpdateStudentLeave---");
        return operationResult;
    }

    /**
     * 根据班级号查询所有打卡规则
     * @param getAllRuleinfoByCourseNumberdto
     */
    @PostMapping("/getAllRuleinfoByCourseNumber")
    @ResponseBody
    public GetAllRuleinfoByCourseNumberresult getAllRuleinfoByCourseNumber(@RequestBody GetAllRuleinfoByCourseNumberdto getAllRuleinfoByCourseNumberdto){
        logger.info("courseNumber:"+getAllRuleinfoByCourseNumberdto.getCourseNumber());
        // 根据班级号查询班级信息
        Courseinfo courseinfo = (Courseinfo)redisUtil.get(RedisConst.COURSEINFO+getAllRuleinfoByCourseNumberdto.getCourseNumber());
        if(courseinfo == null){
            GetAllRuleinfoByCourseNumberresult result = new GetAllRuleinfoByCourseNumberresult();
            result.setCode(1);
            result.setMessage("该班级不存在");
            return result;
        }
        Set<Object> rules = redisUtil.sGet(RedisConst.RULEINFOS+":"+getAllRuleinfoByCourseNumberdto.getCourseNumber());
        List<Ruleinfo> ruleinfos = new ArrayList<>();
        for (Object o:rules) {
            ruleinfos.add((Ruleinfo) o);
        }
        Collections.sort(ruleinfos, new Comparator<Ruleinfo>() {
            @Override
            public int compare(Ruleinfo o1, Ruleinfo o2) {
                return new Long(o2.getRuleNumber()-o1.getRuleNumber()).intValue();
            }
        });

        GetAllRuleinfoByCourseNumberresult result = new GetAllRuleinfoByCourseNumberresult();
        result.setCode(0);
        result.setMessage("查询成功");
        result.setRuleinfos(ruleinfos);
        return result;
    }

//    /**
//     * 获取微信数据
//     * @param
//     */
//    @PostMapping("/getWeixinData")
//    @ResponseBody
//    public GetWeixinDataResult getWeixinData(){
//        logger.info("--start getWeixinData---");
//        GetWeixinDataResult result = new GetWeixinDataResult();
//        result.setAppid(WechatUtil.APPID);
//        result.setSecret(WechatUtil.SECRET);
//        return result;
//    }
    /**
     * 用前端请求接口获取的code换取用户手机号
     * 前端需要请求的接口：https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/getPhoneNumber.html
     * @param weChatPhone
     * @return
     */
    @ResponseBody
    @PostMapping("/phone")
    public WeChatPhoneInfo getPhone(@RequestBody WeChatPhoneDTO weChatPhone){
        logger.info("--start getPhone---");
        // 1.请求微信接口服务，获取accessToken
        weChatPhone.setAppid(WechatUtil.APPID);
        weChatPhone.setSecretKey(WechatUtil.SECRET);
        JSONObject accessTokenJson = WechatUtil.getAccessToken(weChatPhone.getAppid(), weChatPhone.getSecretKey());
        String accessToken = accessTokenJson.getString("access_token");
        // 2.请求微信接口服务，获取用户手机号信息
        JSONObject phoneNumberJson = WechatUtil.getPhoneNumber(weChatPhone.getCode(), accessToken);

        WeChatPhoneInfo phoneInfo = new WeChatPhoneInfo();
        JSONObject phone_info = (JSONObject)phoneNumberJson.get("phone_info");
        if(phone_info != null){
            phoneInfo.setCountryCode(phone_info.getString("countryCode"));
            phoneInfo.setPhoneNumber(phone_info.getString("phoneNumber"));
            phoneInfo.setPurePhoneNumber(phone_info.getString("purePhoneNumber"));
        }
        logger.info("--end getPhone---");
        return phoneInfo;
    }
}