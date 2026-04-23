import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

public class SchedulerGUI extends JFrame {
    DefaultTableModel model;
    JTable table;
    JTextArea output;
    JComboBox<String> algo;

    public SchedulerGUI() {
        setTitle("CPU Scheduling Simulator");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"PID","Arrival","Burst","Priority"},0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel top = new JPanel();
        JButton addBtn = new JButton("Add Process");
        JButton runBtn = new JButton("Run");
        algo = new JComboBox<>(new String[]{"FCFS","SJF","Priority","Round Robin"});
        top.add(addBtn); top.add(algo); top.add(runBtn);
        add(top, BorderLayout.NORTH);

        output = new JTextArea();
        add(new JScrollPane(output), BorderLayout.SOUTH);

        addBtn.addActionListener(e -> model.addRow(new Object[]{model.getRowCount()+1,0,0,0}));
        runBtn.addActionListener(e -> runAlgo());

        setVisible(true);
    }

    void runAlgo() {
        java.util.List<Process> list = new ArrayList<>();
        for (int i=0;i<model.getRowCount();i++) {
            list.add(new Process(
                    Integer.parseInt(model.getValueAt(i,0).toString()),
                    Integer.parseInt(model.getValueAt(i,1).toString()),
                    Integer.parseInt(model.getValueAt(i,2).toString()),
                    Integer.parseInt(model.getValueAt(i,3).toString())
            ));
        }
        String selected = algo.getSelectedItem().toString();
        if(selected.equals("FCFS")) list = SchedulerAlgorithms.fcfs(list);
        else if(selected.equals("SJF")) list = SchedulerAlgorithms.sjf(list);
        else if(selected.equals("Priority")) list = SchedulerAlgorithms.priority(list);
        else list = SchedulerAlgorithms.roundRobin(list, 2);
        StringBuilder sb = new StringBuilder();
        double tw=0, tt=0;
        for (Process p : list) {
            sb.append("P").append(p.pid)
                    .append(" WT=").append(p.waiting)
                    .append(" TAT=").append(p.turnaround).append("\n");
            tw += p.waiting; tt += p.turnaround;
        }
        sb.append("Avg WT: ").append(tw/list.size()).append("\n");
        sb.append("Avg TAT: ").append(tt/list.size());
        output.setText(sb.toString());
    }
}