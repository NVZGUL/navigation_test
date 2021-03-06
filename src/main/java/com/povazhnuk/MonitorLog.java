package com.povazhnuk;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MonitorLog implements Runnable{

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
        File file = new File("files/avg_" + this.file.getName());
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        StringBuilder str = new StringBuilder();
        str.append(dtf.format(now));
        str.append("\n\n");
        for (UserNavigation i: lst) {
            str.append(i.toString());
            str.append('\n');
        }
        bw.write(str.toString());
        bw.close();
    }

    @Override
    public void run(){
        try {
            parseFile();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public void parseFile() throws IOException {
        List<UserNavigation> lst = this.readFromFile();
        lst.sort(Comparator.comparing(UserNavigation::getNumberOfSeconds).reversed());
        Map<UserNavigation, List<Double>> usrUrlBuffer = new HashMap<>();

        // Better solution then double loop
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

        //usrUrlBuffer.keySet().forEach(System.out::println);

        this.writeToFile(usrUrlBuffer.keySet());
    }

    private double avarageSeconds(List<Double> lst) {
        return lst.stream().mapToDouble(a -> a).average().getAsDouble();
    }

    public static void main(String[] args) throws IOException{
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (String fileName : args) {
            service.submit(new MonitorLog(fileName));
        }
        service.shutdown();
    }
}
