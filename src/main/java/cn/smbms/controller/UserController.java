package cn.smbms.controller;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.role.RoleServiceImpl;
import cn.smbms.service.user.UserService;
import cn.smbms.service.user.UserServiceImpl;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.convert.support.ConvertingPropertyEditorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/user")

public class UserController extends BasicController {
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;

    /**
     * 跳转登录页面的方法
     * @return
     */
    @RequestMapping("/login.html")
    public String login(){
        return "login";
    }

    /**
     * 注销页面的方法
     * redirect: 指示符  重定向
     * @return
     */
    @RequestMapping("/logout.html")
    public String logout(HttpSession session){
        session.removeAttribute(Constants.USER_SESSION);//清空session
        return "redirect:/user/login.html";
    }
    /**
     * 处理登录页面
     * @return
     */
    @RequestMapping(value = "/login.html",method = RequestMethod.POST)
    public String doLogin(String userCode, String userPassword, HttpSession session, HttpServletRequest request){
        User user=userService.login(userCode,userPassword);
        if(user!=null){
            session.setAttribute(Constants.USER_SESSION,user);
            return "redirect:/user/frame.html";
        }else{
            request.setAttribute("error","用户名和密码不符");
            return "login";
        }
    }

    /**
     * 跳转到frame页面  权限管理  没有登录是不能直接访问
     * * @return
     */
    @RequestMapping("/frame.html")
    public String frame(HttpSession session){
        User user=(User) session.getAttribute(Constants.USER_SESSION);
//        int num=5/0;
        if(user==null){
            return "login";
        }
        return  "frame";
    }

    /*  *//**
     * 局部异常  本类的异常可以捕获，不能捕获其他的类
     * @return
     *//*
    @ExceptionHandler(value = RuntimeException.class)
    public String exceptionHandler(RuntimeException e,HttpSession session){
        session.setAttribute("e",e.getMessage());
        return "error";
    }*/

    @RequestMapping("/userlist.html")
    public String main(Model model, String queryname, @RequestParam(value = "queryUserRole",defaultValue = "0") Integer userRole,
                       @RequestParam(value = "pageIndex",defaultValue = "1") Integer pageIndex){
        //查询用户列表
        List<User> userList = null;
        //设置页面容量
        int pageSize = Constants.pageSize;
        //当前页码
        System.out.println("queryUserName servlet--------"+queryname);
        System.out.println("queryUserRole servlet--------"+userRole);
        System.out.println("query pageIndex--------- > " + pageIndex);
        //总数量（表）
        int totalCount	= userService.getUserCount(queryname,userRole);
        //总页数
        PageSupport pages=new PageSupport();
        pages.setCurrentPageNo(pageIndex);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();
        //控制首页和尾页
        if(pageIndex < 1){
            pageIndex = 1;
        }else if(pageIndex > totalPageCount){
            pageIndex = totalPageCount;
        }
        userList = userService.getUserList(queryname,userRole,pageIndex, pageSize);
        model.addAttribute("userList", userList);
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUserName", queryname);
        model.addAttribute("queryUserRole", userRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", pageIndex);
        return "userlist";
    }

    /**
     * 跳转添加页面
     * @return
     */
    @RequestMapping("/addUser.html")
    public  String addUser(@ModelAttribute User user){
        return  "useradd";
    }




    /**
     * 处理添加的功能
     * @return
     */
  /*  @RequestMapping(value = "/addUser.html",method = RequestMethod.POST)
    public  String saveUser(User user,HttpSession session){
        user.setCreationDate(new Date());//创建时间
        //创建者
        User user_login= (User) session.getAttribute(Constants.USER_SESSION);
        user.setCreatedBy(user_login.getId());
        if(userService.add(user)){
            return "redirect:/user/userlist.html";
        }
        return  "user/useradd";
    }*/

    /*  *//**
     * 使用JSR 303验证
     * @param user
     * @param session
     * @return
     *//*
    @RequestMapping(value = "/addUser.html",method = RequestMethod.POST)
    public  String saveUser(@Valid User user, BindingResult bindingResult, HttpSession session){
        if(bindingResult.hasErrors()){
            return "user/useradd";
        }
        user.setCreationDate(new Date());//创建时间
        //创建者
        User user_login= (User) session.getAttribute(Constants.USER_SESSION);
        user.setCreatedBy(user_login.getId());
        if(userService.add(user)){
            return "redirect:/user/userlist.html";
        }
        return  "user/useradd";
    }*/


    /**
     * 单文件上传
     * @param user
     * @return
     *//*
    @RequestMapping(value = "/addUser.html",method = RequestMethod.POST)
    public  String saveUser(User user, HttpServletRequest request, @RequestParam(value = "attan",required = false)MultipartFile attan){
        String picPath="";//图片路径
        if(!attan.isEmpty()){//判断是否有上传文件
            //File.separator  文件的系统自适应分隔符
            String path=request.getServletContext().getRealPath("/statics"+ File.separator+"fileUpload");//文件上传的路径  上传到哪里去
            System.out.println("路径是：====================>"+path);
            if(attan.getSize()>5000000){//判断文件大小
                request.setAttribute("error","文件太大了，上传失败");
                return "useradd";
            }
            List<String> suffexs= Arrays.asList(new String[]{".jpg",".png",".jpeg",".gif"});
            String oldFileName=attan.getOriginalFilename();//获取文件名
            //获取后缀
            String suffex=oldFileName.substring(oldFileName.lastIndexOf("."),oldFileName.length());
            System.out.println("源文件的后缀："+suffex);
            if(!suffexs.contains(suffex)){
                request.setAttribute("error","文件类型错误，上传失败");
                return "useradd";
            }
            //文件的重命名    1.解决重名问题2.解决中文乱码
            //重命名点规则是：当前时间的毫秒+1000000的随机数
            String newFileName=System.currentTimeMillis()+""+new Random().nextInt(100000)+suffex;
            File file=new File(path,newFileName);
            if(!file.exists()){
                file.mkdirs();
            }
            try {
                attan.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
                request.setAttribute("error","上传失败");
                return "useradd";
            }
            picPath=path+File.separator+newFileName;//图片路径
        }
        user.setPicPath(picPath);
        user.setCreationDate(new Date());//创建时间
        //创建者
        User user_login= (User) request.getSession().getAttribute(Constants.USER_SESSION);
        user.setCreatedBy(user_login.getId());
        if(userService.add(user)){
            return "redirect:/user/userlist.html";
        }
        return  "useradd";
    }*/

    /**
     * 多文件上传
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(value = "/addUser.html",method = RequestMethod.POST)
    public  String saveUser(User user, HttpServletRequest request,
                            @RequestParam(value = "attan",required = false)MultipartFile attan,
                            @RequestParam(value = "attan_work",required = false)MultipartFile attan_work){
        String picPath=uploadFile(request,attan);//图片路径
        String workPicPath=uploadFile(request,attan_work);//工作照
        if(picPath==null||workPicPath==null){
            return "useradd";
        }
        user.setWorkPicPath(workPicPath);
        user.setPicPath(picPath);
        user.setCreationDate(new Date());//创建时间
        //创建者
        User user_login= (User) request.getSession().getAttribute(Constants.USER_SESSION);
        user.setCreatedBy(user_login.getId());
        if(userService.add(user)){
            return "redirect:/user/userlist.html";
        }
        return  "useradd";
    }


    /**
     *文件上传的方法
     * * @param request
     * @param attan
     * @return null  是上传失败
     *
     */
    public String uploadFile(HttpServletRequest request,MultipartFile attan) {
        String picPath="";
        if (!attan.isEmpty()) {//判断是否有上传文件
            //File.separator  文件的系统自适应分隔符
            String path = request.getServletContext().getRealPath("/statics" + File.separator + "fileUpload");//文件上传的路径  上传到哪里去
            System.out.println("路径是：====================>" + path);
            if (attan.getSize() > 5000000) {//判断文件大小
                request.setAttribute("error", "文件太大了，上传失败");
                return null;
            }
            List<String> suffexs = Arrays.asList(new String[]{".jpg", ".png", ".jpeg", ".gif"});
            String oldFileName = attan.getOriginalFilename();//获取文件名
            //获取后缀
            String suffex = oldFileName.substring(oldFileName.lastIndexOf("."), oldFileName.length());
            System.out.println("源文件的后缀：" + suffex);
            if (!suffexs.contains(suffex)) {
                request.setAttribute("error", "文件类型错误，上传失败");
                return null;
            }
            //文件的重命名    1.解决重名问题2.解决中文乱码
            //重命名点规则是：当前时间的毫秒+1000000的随机数
            String newFileName = System.currentTimeMillis() + "" + new Random().nextInt(100000) + suffex;
            File file = new File(path, newFileName);
            if (!file.exists()) {
                file.mkdirs();
            }
            try {
                attan.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
                request.setAttribute("error", "上传失败");
                return null;
            }
            picPath= newFileName;//图片路径
        }
        return picPath;
    }
    /**
     修改的方法
     * @return
     */
    @RequestMapping("/modify.html")
    public String modify(String uid,Model model){
        User user=userService.getUserById(uid);
        model.addAttribute("user",user);

        return "usermodify";
    }

    /**
     * 保存修改的方法
     * @param user
     * @param session
     * @return
     */
    @RequestMapping(value = "/modify.html",method = RequestMethod.POST)
    public String saveModify(User user,HttpSession session){
        System.out.println("============>");
        user.setModifyDate(new Date());//创建时间
        //创建者
        User user_login= (User) session.getAttribute(Constants.USER_SESSION);
        user.setModifyBy(user_login.getId());
        System.out.println(user);
        if(userService.modify(user)){
            return "redirect:/user/userlist.html";
        }
        return "usermodify";
    }

/*    @RequestMapping(value = "/view.html/{id}")
    public String view(@PathVariable String id,Model model){
        User user=  userService.getUserById(id);
//        String picPath=user.getPicPath();
//        user.setPicPath(picPath.substring(picPath.lastIndexOf("\\")+1));
        model.addAttribute("user",user);
        return "userview";
    }*/


    /*   @RequestMapping(value = "/view"*//*,produces = {"application/json;charset=UTF-8"}*//*)
    @ResponseBody
    public Object view( String id,Model model){
        User user=  userService.getUserById(id);
        model.addAttribute("user",user);
        return JSONArray.toJSONString(user);
    }*/
    @RequestMapping(value = "/view")
    @ResponseBody
    public User view( String id,Model model){
        User user=  userService.getUserById(id);
        model.addAttribute("user",user);
        return user;
    }

    @RequestMapping(value = "/isExits")
    @ResponseBody
    public User isExist(String userCode){
        User user=userService.selectUserCodeExist(userCode);
        return user;
    }


    @RequestMapping(value = "/roles")
    @ResponseBody
    public List<Role> roles(){
        return roleService.getRoleList();
    }


}
