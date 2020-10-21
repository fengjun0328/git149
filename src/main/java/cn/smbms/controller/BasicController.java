package cn.smbms.controller;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BasicController {

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        try {
            dataBinder.registerCustomEditor(Date.class,new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
