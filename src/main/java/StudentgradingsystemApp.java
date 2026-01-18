import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.Timer;

public class StudentgradingsystemApp extends JFrame {

    // ===== Grading Fields =====
    private JTextField nameField, rollField;
    private JTextField[] subjectFields;
    private JTextArea resultArea;

    // ===== CGPA Fields =====
    private JTextField numSubjectsField;
    private JPanel gradesContainer;
    private JTextArea cgpaResult;

    // ===== Engineering Cutoff Fields =====
    private JTextField mathsField, physicsField, chemistryField;
    private JTextArea cutoffResult;

    public StudentgradingsystemApp() {
        setTitle("🎓 Student Grading System");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Title =====
        JLabel title = new JLabel("STUDENT GRADING SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(60, 20, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        fadeInLabel(title); // animation

        // ===== Tabbed Pane =====
        JTabbedPane tabs = new JTabbedPane();

        // ===== Student Grading Tab =====
        JPanel gradingPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        gradingPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        gradingPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        gradingPanel.add(nameField);

        gradingPanel.add(new JLabel("Roll Number:"));
        rollField = new JTextField();
        gradingPanel.add(rollField);

        // Dynamic Subject Fields (default 6)
        subjectFields = new JTextField[6];
        for (int i = 0; i < 6; i++) {
            gradingPanel.add(new JLabel("Subject " + (i + 1) + " Marks:"));
            subjectFields[i] = new JTextField();
            gradingPanel.add(subjectFields[i]);
        }

        JButton calcBtn = new JButton("Calculate Result");
        JButton clearBtn = new JButton("Clear");

        gradingPanel.add(calcBtn);
        gradingPanel.add(clearBtn);

        resultArea = new JTextArea();
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createTitledBorder("Result"));

        JPanel gradingContainer = new JPanel(new BorderLayout());
        gradingContainer.add(gradingPanel, BorderLayout.CENTER);
        gradingContainer.add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        tabs.add("Student Grading", gradingContainer);

        // ===== Engineering Cut-off Tab =====
        JPanel cutoffPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        cutoffPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        mathsField = new JTextField();
        physicsField = new JTextField();
        chemistryField = new JTextField();
        cutoffResult = new JTextArea();
        cutoffResult.setEditable(false);

        cutoffPanel.add(new JLabel("Maths Marks:"));
        cutoffPanel.add(mathsField);
        cutoffPanel.add(new JLabel("Physics Marks:"));
        cutoffPanel.add(physicsField);
        cutoffPanel.add(new JLabel("Chemistry Marks:"));
        cutoffPanel.add(chemistryField);

        JButton calcCutoffBtn = new JButton("Calculate Cut-off");
        JButton clearCutoffBtn = new JButton("Clear");
        cutoffPanel.add(calcCutoffBtn);
        cutoffPanel.add(clearCutoffBtn);

        JPanel cutoffContainer = new JPanel(new BorderLayout());
        cutoffContainer.add(cutoffPanel, BorderLayout.NORTH);
        cutoffContainer.add(new JScrollPane(cutoffResult), BorderLayout.CENTER);

        tabs.add("Engineering Cut-off", cutoffContainer);

        // ===== CGPA Calculator Tab =====
        JPanel cgpaPanel = new JPanel(new BorderLayout());
        cgpaPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JPanel cgpaInputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        cgpaInputPanel.add(new JLabel("Number of Subjects:"));
        numSubjectsField = new JTextField();
        cgpaInputPanel.add(numSubjectsField);

        JButton generateFieldsBtn = new JButton("Generate Grade Fields");
        cgpaInputPanel.add(generateFieldsBtn);

        cgpaPanel.add(cgpaInputPanel, BorderLayout.NORTH);

        gradesContainer = new JPanel(new GridLayout(0, 2, 10, 10));
        cgpaPanel.add(gradesContainer, BorderLayout.CENTER);

        cgpaResult = new JTextArea();
        cgpaResult.setEditable(false);
        cgpaResult.setBorder(BorderFactory.createTitledBorder("CGPA Result"));
        cgpaPanel.add(new JScrollPane(cgpaResult), BorderLayout.SOUTH);

        tabs.add("CGPA Calculator", cgpaPanel);

        add(tabs, BorderLayout.CENTER);

        // ===== Button Actions =====
        calcBtn.addActionListener(e -> calculateGrading());
        clearBtn.addActionListener(e -> clearGrading());

        calcCutoffBtn.addActionListener(e -> calculateCutoff());
        clearCutoffBtn.addActionListener(e -> clearCutoff());

        generateFieldsBtn.addActionListener(e -> generateGradeFields());

        setVisible(true);
    }

    // ===== Animations =====
    private void fadeInLabel(JLabel label) {
        Timer timer = new Timer(50, null);
        timer.addActionListener(e -> {
            Color c = label.getForeground();
            if (c.getRed() < 255) {
                label.setForeground(new Color(c.getRed() + 5, c.getGreen(), c.getBlue()));
            } else {
                timer.stop();
            }
        });
        timer.start();
    }

    private void typingEffect(JTextArea area, String text) {
        Timer timer = new Timer(50, null);
        final int[] index = {0};
        timer.addActionListener(e -> {
            if (index[0] < text.length()) {
                area.append(text.charAt(index[0]) + "");
                index[0]++;
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    // ===== Student Grading Calculation =====
    private void calculateGrading() {
        try {
            String name = nameField.getText();
            String roll = rollField.getText();

            int total = 0;
            int count = 0;
            StringBuilder marksStr = new StringBuilder();
            for (int i = 0; i < subjectFields.length; i++) {
                int mark = Integer.parseInt(subjectFields[i].getText());
                if (mark < 0 || mark > 100) {
                    JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100");
                    return;
                }
                total += mark;
                count++;
                marksStr.append("\nSubject ").append(i + 1).append(" : ").append(mark);
            }
            double avg = total * 1.0 / count;
            String grade = (avg >= 90) ? "A+" :
                           (avg >= 80) ? "A" :
                           (avg >= 70) ? "B" :
                           (avg >= 60) ? "C" :
                           (avg >= 50) ? "D" : "FAIL";

            String result = "Student Name : " + name +
                            "\nRoll Number : " + roll +
                            marksStr.toString() +
                            "\n\nTotal   : " + total +
                            "\nAverage : " + String.format("%.2f", avg) +
                            "\nGrade   : " + grade;

            resultArea.setText("");
            typingEffect(resultArea, result);

            saveResultToFile(name, roll, marksStr.toString(), total, avg, grade);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!");
        }
    }

    private void clearGrading() {
        nameField.setText("");
        rollField.setText("");
        for (JTextField f : subjectFields) f.setText("");
        resultArea.setText("");
    }

    // ===== Engineering Cutoff =====
    private void calculateCutoff() {
        try {
            int m = Integer.parseInt(mathsField.getText());
            int p = Integer.parseInt(physicsField.getText());
            int c = Integer.parseInt(chemistryField.getText());
            double cutoff = m / 2.0 + p / 4.0 + c / 4.0;
            cutoffResult.setText("Engineering Cut-off: " + String.format("%.2f", cutoff));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers!");
        }
    }

    private void clearCutoff() {
        mathsField.setText("");
        physicsField.setText("");
        chemistryField.setText("");
        cutoffResult.setText("");
    }

    // ===== CGPA =====
    private void generateGradeFields() {
        gradesContainer.removeAll();
        try {
            int n = Integer.parseInt(numSubjectsField.getText());
            JTextField[] gradeFields = new JTextField[n];
            for (int i = 0; i < n; i++) {
                gradesContainer.add(new JLabel("Subject " + (i + 1) + " Grade (O,A+,A,B+,B,C,P,F):"));
                gradeFields[i] = new JTextField();
                gradesContainer.add(gradeFields[i]);
            }
            JButton calcCGPA = new JButton("Calculate CGPA");
            gradesContainer.add(calcCGPA);
            gradesContainer.revalidate();
            gradesContainer.repaint();

            calcCGPA.addActionListener(e -> {
                double total = 0;
                for (JTextField f : gradeFields) {
                    String grade = f.getText().toUpperCase();
                    total += switch (grade) {
                        case "O" -> 10;
                        case "A+" -> 9;
                        case "A" -> 8.5;
                        case "B+" -> 8;
                        case "B" -> 7;
                        case "C" -> 6;
                        case "P" -> 5;
                        case "F" -> 0;
                        default -> 0;
                    };
                }
                double cgpa = n > 0 ? total / n : 0;
                cgpaResult.setText("Your CGPA is: " + String.format("%.2f", cgpa));
            });

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Enter valid number!");
        }
    }

    // ===== Save Result =====
    private void saveResultToFile(String name, String roll, String marks, int total, double avg, String grade) {
        try (FileWriter fw = new FileWriter("StudentResults.txt", true)) {
            fw.write("\n--- Student ---\n");
            fw.write("Name: " + name + "\nRoll: " + roll + marks +
                     "\nTotal: " + total + "\nAverage: " + avg + "\nGrade: " + grade + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ===== Main =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentgradingsystemApp::new);
    }
}