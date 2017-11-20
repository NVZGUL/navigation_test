package com.povazhnuk;

import java.io.*;
import java.util.*;
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
        return UserNavigation.newBuilder()
                .setID(Long.parseLong(p[0]))
                .setUser(p[1])
                .setURL(p[2])
                .setNumberOfSeconds(Integer.parseInt(p[3]))
                .build();
    };

    public List<UserNavigation> readFromFile() throws IOException {
        List<UserNavigation> il;
        InputStream is = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        il = br.lines().skip(1).map(mapToUserNavigation).collect(Collectors.toList());
        br.close();
        return il;
    }

    public void writeToFile(Set<UserNavigation> lst) throws IOException {
        File file = new File("avg_" + this.file.getName());
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        String str = "";
        for (UserNavigation i: lst) {
            str += i.toString() + '\n';
        }
        bw.write(str);
        bw.close();
    }

    public void run() throws IOException {
        List<UserNavigation> lst = this.readFromFile();
        lst.sort(Comparator.comparing(UserNavigation::getNumberOfSeconds).reversed());
        Map<UserNavigation, List<Double>> usrUrlBuffer = new HashMap<>();

        for (UserNavigation i : lst) {
            List<Double> tmp = new ArrayList<>();
            if (usrUrlBuffer.containsKey(i)) {
                tmp = usrUrlBuffer.get(i);
                tmp.add(i.getNumberOfSeconds());
                UserNavigation tmpUN = UserNavigation.newBuilder()
                                        .setUser(i.getUser())
                                        .setURL(i.getURL())
                                        .setNumberOfSeconds(this.avarageSeconds(tmp))
                                        .build();
                usrUrlBuffer.remove(i);
                usrUrlBuffer.put(tmpUN, tmp);
            }
            else {
                tmp.add(i.getNumberOfSeconds());
                usrUrlBuffer.put(i, tmp);
            }
        }
        for (UserNavigation i : usrUrlBuffer.keySet()) {
            System.out.println(i);
        }
        this.writeToFile(usrUrlBuffer.keySet());
    }

    private double avarageSeconds(List<Double> lst) {
        return lst.stream().mapToDouble(a -> a).average().getAsDouble();
    }

    public static void main(String[] args) throws IOException{
        MonitorLog mL = new MonitorLog("test.csv");
        mL.run();
    }
}
