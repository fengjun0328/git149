package cn.smbms.tools;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToDateConverter  implements Converter<String, Date> {

    private String pattan;

    public StringToDateConverter(String pattan) {
        this.pattan = pattan;
    }

    @Override
    public Date convert(String source){
        Date date=null;
        SimpleDateFormat format=new SimpleDateFormat(pattan);
        try {
            date=format.parse(source);
            return  date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
