package com.povazhnuk;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MonitorLog {

    private final String COMMA = ",";

    private File file;

    public MonitorLog(String filePath) {
        this.file = new File(filePath);
    }

    private Function<String, UserNavigation> mapToUserNavigation = (line) -> {
        String[] p = line.split(COMMA);
        UserNavigation item = UserNavigation.newBuilder()
                                .setID(Long.parseLong(p[0]))
                                .setUser(p[1])
                                .setURL(p[2])
                                .setNumberOfSeconds(Integer.parseInt(p[3]))
                                .build();
        return item;
    };

    public List<UserNavigation> run() throws IOException {
        List<UserNavigation> il = new ArrayList<>();
        InputStream is = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        il = br.lines().skip(1).map(mapToUserNavigation).collect(Collectors.toList());
        br.close();
        return il;
    }

    public static void main(String[] args) throws IOException{
        UserNavigation uN = UserNavigation.newBuilder()
                                .setID(12121)
                                .setUser("user")
                                .setURL("http://test")
                                .setNumberOfSeconds(10)
                                .build();
        System.out.println(uN);
        MonitorLog mL = new MonitorLog("test.csv");
        for (UserNavigation i : mL.run()) System.out.println(i);;
    }
}
