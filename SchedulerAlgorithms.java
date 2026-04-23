import java.util.*;

public class SchedulerAlgorithms {

    public static List<Process> sjf(List<Process> list) {
        list.sort(Comparator.comparingInt((Process p) -> p.arrival)
                .thenComparingInt(p -> p.burst));

        int time = 0;

        for (Process p : list) {
            if (time < p.arrival)
                time = p.arrival;

            p.waiting = time - p.arrival;
            time += p.burst;
            p.completion = time;
            p.turnaround = p.completion - p.arrival;
        }

        return list;
    }

    public static List<Process> priority(List<Process> list) {
        list.sort(Comparator.comparingInt((Process p) -> p.arrival)
                .thenComparingInt(p -> p.priority));

        int time = 0;

        for (Process p : list) {
            if (time < p.arrival)
                time = p.arrival;

            p.waiting = time - p.arrival;
            time += p.burst;
            p.completion = time;
            p.turnaround = p.completion - p.arrival;
        }

        return list;
    }

    public static List<Process> roundRobin(List<Process> list, int quantum) {

        Queue<Process> q = new LinkedList<>();

        list.sort(Comparator.comparingInt(p -> p.arrival));

        Map<Integer, Integer> rem = new HashMap<>();

        for (Process p : list)
            rem.put(p.pid, p.burst);

        int time = 0, i = 0, done = 0;

        while (done < list.size()) {

            while (i < list.size() && list.get(i).arrival <= time)
                q.add(list.get(i++));

            if (q.isEmpty()) {
                time++;
                continue;
            }

            Process p = q.poll();

            int run = Math.min(quantum, rem.get(p.pid));

            time += run;

            rem.put(p.pid, rem.get(p.pid) - run);

            while (i < list.size() && list.get(i).arrival <= time)
                q.add(list.get(i++));

            if (rem.get(p.pid) > 0)
                q.add(p);
            else {
                p.completion = time;
                p.turnaround = p.completion - p.arrival;
                p.waiting = p.turnaround - p.burst;
                done++;
            }
        }

        return list;
    }

    public static List<Process> fcfs(List<Process> list) {

        list.sort(Comparator.comparingInt(p -> p.arrival));

        int time = 0;

        for (Process p : list) {
            if (time < p.arrival)
                time = p.arrival;

            p.waiting = time - p.arrival;
            time += p.burst;
            p.completion = time;
            p.turnaround = p.completion - p.arrival;
        }

        return list;
    }
}