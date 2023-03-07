package cn.ityao.wall;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class test {

    public static void main(String[] args) throws Exception {

        /*InetAddress addr = InetAddress.getLocalHost();
        System.out.println("Local HostAddress:"+addr.getHostAddress());

        String hostname = addr.getHostName();
        System.out.println("Local host name: "+hostname);*/


        Date date = new Date();
        //date.setTime(1632812400);
        //1610199034
        //1632807975442

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(date));



        String aaa = "2021-09-28 15:00:00";
        date = simpleDateFormat.parse(aaa);
        long lTime = date.getTime() / 1000;
        System.out.println(lTime);


    }
}
