import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


// todo (mark as done)
// todo (move all classes in sepatate classes)


public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        MainFrame frame = new MainFrame("Task Manager", manager.getAllTasks(), manager);
        frame.showFrame();
    }
}


class MainFrame {

    private JFrame frame; 
    private DefaultTableModel taskTableModel;
    private JTable taskTable;
    private Vector<String> columnNames;
    private TaskManager manager;
    
    MainFrame(String title, Vector<Task> tasks, TaskManager manager) {
        this.manager = manager;
        this.columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add("Title");
        columnNames.add("Body");
        columnNames.add("Status");
        setUpFrame(title);
    }

    private JFrame setUpFrame(String title) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        putContent(frame);
        
        frame.pack();

        return frame;
    }

    private void putContent(JFrame frame) {
        showTasksOnFrame(frame);
    }

    private void showTasksOnFrame(JFrame frame) {
        Vector<Vector<Object>> dataVector = new Vector<>();

        for (Task task : manager.getAllTasks()) {
            Vector<Object> row = new Vector<>();
            row.add(task.getTaskId());
            row.add(task.getTaskTitle());
            row.add(task.getTaskBody());
            row.add(task.getTaskStatus());
            dataVector.add(row);
        }

        taskTableModel = new DefaultTableModel(dataVector, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        taskTable = new JTable(taskTableModel);
        frame.getContentPane().removeAll();

        JScrollPane scrollPane = new JScrollPane(taskTable);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        taskTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = taskTable.rowAtPoint(e.getPoint());

                    if (row != -1) {
                        taskTable.setRowSelectionInterval(row, row);

                        JPopupMenu menu = new JPopupMenu();
                        JMenuItem deleteItem = new JMenuItem("Delete task");
                        deleteItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String id = (String) taskTable.getValueAt(row, 0);
                                manager.removeTask(id);
                                showTasksOnFrame(frame);
                            }
                        });
                        menu.add(deleteItem);
                        menu.show(taskTable, e.getX(), e.getY());
                    }
                }
            }
        });

        scrollPane.getViewport().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem addItem = new JMenuItem("Add task");
                    addItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JPanel panel = new JPanel(new GridLayout(2, 2));
                            panel.add(new JLabel("Title:"));
                            JTextField titleField = new JTextField();
                            panel.add(titleField);
                            panel.add(new JLabel("Body:"));
                            JTextField bodyField = new JTextField();
                            panel.add(bodyField);

                            int result = JOptionPane.showConfirmDialog(null, panel, "Add Task", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                            if (result == JOptionPane.OK_OPTION) {
                                String title = titleField.getText();
                                String body = bodyField.getText();
                                Task newTask = new Task(title, body);
                                manager.addTask(newTask);
                                showTasksOnFrame(frame);
                            }
                        }
                    });
                    menu.add(addItem);
                    menu.show(scrollPane, e.getX(), e.getY());
                }
            }
        });

        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    public void showFrame() {
        frame.setVisible(true);
    }
}


final class Task {
   
    private long timestamp = System.currentTimeMillis();
    private int randomNumber;
    private String taskId;
    private Random rand = new Random();
    
    public int taskStatus;

    private String title;

    private String body;


    Task(String title, String body) {
        this.title = title;
        this.body = body;
        this.randomNumber = generateRandomNumber();
        this.taskId = timestamp + "_" + randomNumber;
        this.taskStatus = 0;
    }

    public String getTaskTitle() {
        return title;
    }

    public String getTaskBody() {
        return body;
    }

 
    public int getTaskStatus() {
        return taskStatus;
    }


    public String getTaskId() {
        return taskId;
    }

    @Override
    public String toString() {
        String status = (this.taskStatus == 1) ? "Done" : "Undone";
        return "ID: " + taskId + "; " +
               "Title: " + this.title + "; " +
               "Body: " + this.body + "; " +
               "Status: " + status;
    }

    private int generateRandomNumber() {
        return rand.nextInt(10000);
    }
}


class TaskManager {

    private Vector<Task> tasks;


    TaskManager() {
        this.tasks = new Vector<>();    
    } 

    public void addTask(Task task) {
        tasks.add(task);
    }


    public Vector<Task> getAllTasks() {
        if (tasks == null) {
            tasks = new Vector<>();
        }
        return tasks;
    }


    public void markAsDone(Task task) {
        task.taskStatus = 1;     
    }

    
    private int getTaskById(String id) {
        int index = -1;

        for (Task task: tasks) {
            String currId = task.getTaskId();
                
            if (currId.equals(id)) {
                index = tasks.indexOf(task);
                break;
            }
        }

        return index;
    }


    public void removeTask(String id) {
        tasks.remove(getTaskById(id));
    }


    @Override
    public String toString() {
        return "This is a TaskManager, current number of tasks is " + tasks.size();
    }
}

















