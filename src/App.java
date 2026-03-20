import controller.AttendanceController;
import controller.AuthController;
import controller.CourseController;
import controller.EnrollmentController;
import controller.ResultController;
import controller.StudentController;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import model.Attendance;
import model.Course;
import model.Enrollment;
import model.Result;
import model.Student;

public class App extends JFrame {
    private static final Color PAGE_BG = new Color(236, 241, 245);
    private static final Color CARD_BG = new Color(255, 255, 255, 235);
    private static final Color PRIMARY = new Color(18, 155, 119);
    private static final Color PRIMARY_DARK = new Color(11, 107, 90);
    private static final Color TEXT_DARK = new Color(31, 42, 55);
    private static final Color TEXT_MUTED = new Color(98, 111, 124);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private final StudentController studentController;
    private final CourseController courseController;
    private final EnrollmentController enrollmentController;
    private final ResultController resultController;
    private final AttendanceController attendanceController;
    private final AuthController authController;

    private final JPanel rootPanel = new JPanel(new CardLayout());
    private final GradientPanel loginPanel = new GradientPanel();
    private final JPanel mainPanel = new JPanel(new BorderLayout(16, 16));

    private final JTextField usernameField = createTextField("admin");
    private final JPasswordField passwordField = createPasswordField("admin");
    private final JLabel loginStatus = new JLabel(" ");

    private final JTextArea studentsArea = createDisplayArea();
    private final JTextArea coursesArea = createDisplayArea();
    private final JTextArea enrollmentsArea = createDisplayArea();
    private final JTextArea resultsArea = createDisplayArea();
    private final JTextArea attendanceArea = createDisplayArea();

    public App() {
        super("Student Management System");
        this.studentController = new StudentController();
        this.courseController = new CourseController();
        this.enrollmentController = new EnrollmentController();
        this.resultController = new ResultController();
        this.attendanceController = new AttendanceController();
        this.authController = new AuthController();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1080, 720));
        setSize(1180, 780);
        setLocationRelativeTo(null);

        initLoginPanel();
        initMainPanel();

        rootPanel.add(loginPanel, "LOGIN");
        rootPanel.add(mainPanel, "MAIN");
        setContentPane(rootPanel);
        showCard("LOGIN");
    }

    private void initLoginPanel() {
        loginPanel.setLayout(new GridBagLayout());

        JPanel loginCard = createGlassCard();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setPreferredSize(new Dimension(430, 470));

        StudentPhotoPanel photoPanel = new StudentPhotoPanel();
        photoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel appTitle = new JLabel("Student Management");
        appTitle.setFont(TITLE_FONT);
        appTitle.setForeground(TEXT_DARK);
        appTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton loginButton = new JButton("Sign In");
        stylePrimaryButton(loginButton);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.addActionListener(this::onLogin);

        loginStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loginStatus.setForeground(new Color(196, 57, 52));
        loginStatus.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginCard.add(photoPanel);
        loginCard.add(Box.createRigidArea(new Dimension(0, 18)));
        loginCard.add(appTitle);
        loginCard.add(Box.createRigidArea(new Dimension(0, 22)));
        loginCard.add(createFieldBlock("Username", usernameField));
        loginCard.add(Box.createRigidArea(new Dimension(0, 16)));
        loginCard.add(createFieldBlock("Password", passwordField));
        loginCard.add(Box.createRigidArea(new Dimension(0, 22)));
        loginCard.add(loginButton);
        loginCard.add(Box.createRigidArea(new Dimension(0, 12)));
        loginCard.add(loginStatus);

        loginPanel.add(loginCard, new GridBagConstraints());
    }

    private void initMainPanel() {
        mainPanel.setBackground(PAGE_BG);
        mainPanel.setBorder(new EmptyBorder(18, 18, 18, 18));
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createDashboardTabs(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Student Management Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(TEXT_DARK);

        JLabel userLabel = new JLabel("Admin");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(TEXT_MUTED);

        titleGroup.add(title);
        titleGroup.add(Box.createRigidArea(new Dimension(0, 4)));
        titleGroup.add(userLabel);

        JPanel rightSide = new JPanel();
        rightSide.setOpaque(false);
        rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.X_AXIS));

        StudentPhotoPanel smallPhoto = new StudentPhotoPanel();
        smallPhoto.setPreferredSize(new Dimension(74, 74));
        smallPhoto.setMaximumSize(new Dimension(74, 74));
        smallPhoto.setMinimumSize(new Dimension(74, 74));

        JButton refreshButton = new JButton("Refresh");
        styleOutlineButton(refreshButton);
        refreshButton.addActionListener(e -> refreshAllData());

        rightSide.add(refreshButton);
        rightSide.add(Box.createRigidArea(new Dimension(14, 0)));
        rightSide.add(smallPhoto);

        header.add(titleGroup, BorderLayout.WEST);
        header.add(rightSide, BorderLayout.EAST);
        return header;
    }

    private JTabbedPane createDashboardTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);

        tabbedPane.add("Students", createStudentPanel());
        tabbedPane.add("Courses", createCoursePanel());
        tabbedPane.add("Enrollments", createEnrollmentPanel());
        tabbedPane.add("Results", createResultPanel());
        tabbedPane.add("Attendance", createAttendancePanel());
        return tabbedPane;
    }

    private JPanel createStudentPanel() {
        JPanel panel = createPagePanel();

        JTextField nameField = createTextField("");
        JTextField emailField = createTextField("");
        JButton addButton = new JButton("Add Student");
        stylePrimaryButton(addButton);

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            if (name.isEmpty() || email.isEmpty()) {
                showError("Please enter both student name and email.");
                return;
            }
            studentController.addStudent(name, email);
            refreshStudents();
            nameField.setText("");
            emailField.setText("");
        });

        panel.add(createFormCard(
                "Student",
                new JComponent[] {nameField, emailField},
                new String[] {"Name", "Email"},
                addButton
        ), BorderLayout.WEST);
        panel.add(createListCard("Student List", studentsArea), BorderLayout.CENTER);

        refreshStudents();
        return panel;
    }

    private JPanel createCoursePanel() {
        JPanel panel = createPagePanel();

        JTextField nameField = createTextField("");
        JTextField creditsField = createTextField("");
        JButton addButton = new JButton("Add Course");
        stylePrimaryButton(addButton);

        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                int credits = Integer.parseInt(creditsField.getText().trim());
                if (name.isEmpty()) {
                    showError("Please enter the course name.");
                    return;
                }
                courseController.addCourse(name, credits);
                refreshCourses();
                nameField.setText("");
                creditsField.setText("");
            } catch (NumberFormatException ex) {
                showError("Credits must be an integer.");
            }
        });

        panel.add(createFormCard(
                "Course",
                new JComponent[] {nameField, creditsField},
                new String[] {"Name", "Credits"},
                addButton
        ), BorderLayout.WEST);
        panel.add(createListCard("Course List", coursesArea), BorderLayout.CENTER);

        refreshCourses();
        return panel;
    }

    private JPanel createEnrollmentPanel() {
        JPanel panel = createPagePanel();

        JTextField studentIdField = createTextField("");
        JTextField courseIdField = createTextField("");
        JTextField semesterField = createTextField("");
        JButton addButton = new JButton("Enroll Student");
        stylePrimaryButton(addButton);

        addButton.addActionListener(e -> {
            try {
                int studentId = Integer.parseInt(studentIdField.getText().trim());
                int courseId = Integer.parseInt(courseIdField.getText().trim());
                String semester = semesterField.getText().trim();
                if (semester.isEmpty()) {
                    showError("Please enter the semester.");
                    return;
                }
                enrollmentController.enrollStudent(studentId, courseId, semester);
                refreshEnrollments();
                studentIdField.setText("");
                courseIdField.setText("");
                semesterField.setText("");
            } catch (NumberFormatException ex) {
                showError("Student ID and Course ID must be integers.");
            }
        });

        panel.add(createFormCard(
                "Enrollment",
                new JComponent[] {studentIdField, courseIdField, semesterField},
                new String[] {"Student ID", "Course ID", "Semester"},
                addButton
        ), BorderLayout.WEST);
        panel.add(createListCard("Enrollment List", enrollmentsArea), BorderLayout.CENTER);

        refreshEnrollments();
        return panel;
    }

    private JPanel createResultPanel() {
        JPanel panel = createPagePanel();

        JTextField enrollmentIdField = createTextField("");
        JTextField gradeField = createTextField("");
        JButton addButton = new JButton("Add Result");
        stylePrimaryButton(addButton);

        addButton.addActionListener(e -> {
            try {
                int enrollmentId = Integer.parseInt(enrollmentIdField.getText().trim());
                String grade = gradeField.getText().trim();
                if (grade.isEmpty()) {
                    showError("Please enter the grade.");
                    return;
                }
                resultController.addResult(enrollmentId, grade);
                refreshResults();
                enrollmentIdField.setText("");
                gradeField.setText("");
            } catch (NumberFormatException ex) {
                showError("Enrollment ID must be an integer.");
            }
        });

        panel.add(createFormCard(
                "Result",
                new JComponent[] {enrollmentIdField, gradeField},
                new String[] {"Enrollment ID", "Grade"},
                addButton
        ), BorderLayout.WEST);
        panel.add(createListCard("Result List", resultsArea), BorderLayout.CENTER);

        refreshResults();
        return panel;
    }

    private JPanel createAttendancePanel() {
        JPanel panel = createPagePanel();

        JTextField enrollmentIdField = createTextField("");
        JTextField dateField = createTextField(LocalDate.now().toString());
        JTextField statusField = createTextField("");
        JButton addButton = new JButton("Add Attendance");
        stylePrimaryButton(addButton);

        addButton.addActionListener(e -> {
            try {
                int enrollmentId = Integer.parseInt(enrollmentIdField.getText().trim());
                LocalDate date = LocalDate.parse(dateField.getText().trim());
                String status = statusField.getText().trim();
                if (status.isEmpty()) {
                    showError("Please enter the attendance status.");
                    return;
                }
                attendanceController.addAttendance(enrollmentId, date, status);
                refreshAttendance();
                enrollmentIdField.setText("");
                dateField.setText(LocalDate.now().toString());
                statusField.setText("");
            } catch (NumberFormatException ex) {
                showError("Enrollment ID must be an integer.");
            } catch (DateTimeParseException ex) {
                showError("Date must be in YYYY-MM-DD format.");
            }
        });

        panel.add(createFormCard(
                "Attendance",
                new JComponent[] {enrollmentIdField, dateField, statusField},
                new String[] {"Enrollment ID", "Date", "Status"},
                addButton
        ), BorderLayout.WEST);
        panel.add(createListCard("Attendance List", attendanceArea), BorderLayout.CENTER);

        refreshAttendance();
        return panel;
    }

    private JPanel createPagePanel() {
        JPanel panel = new JPanel(new BorderLayout(18, 0));
        panel.setBackground(PAGE_BG);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));
        return panel;
    }

    private JPanel createFormCard(String title, JComponent[] fields, String[] labels, JButton actionButton) {
        JPanel card = createGlassCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(320, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_DARK);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 18)));

        for (int i = 0; i < fields.length; i++) {
            card.add(createFieldBlock(labels[i], fields[i]));
            card.add(Box.createRigidArea(new Dimension(0, 14)));
        }

        actionButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(actionButton);
        return card;
    }

    private JPanel createListCard(String title, JTextArea area) {
        JPanel card = createGlassCard();
        card.setLayout(new BorderLayout(0, 14));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_DARK);

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 228, 234)));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    private JPanel createGlassCard() {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 224, 231)),
                new EmptyBorder(22, 22, 22, 22)
        ));
        return panel;
    }

    private JPanel createFieldBlock(String labelText, JComponent field) {
        JPanel block = new JPanel();
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));
        block.setOpaque(false);
        block.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_DARK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        block.add(label);
        block.add(Box.createRigidArea(new Dimension(0, 6)));
        block.add(field);
        return block;
    }

    private JTextField createTextField(String value) {
        JTextField field = new JTextField(value);
        styleInput(field);
        return field;
    }

    private JPasswordField createPasswordField(String value) {
        JPasswordField field = new JPasswordField(value);
        styleInput(field);
        return field;
    }

    private void styleInput(JComponent component) {
        component.setFont(BODY_FONT);
        component.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 216, 223)),
                new EmptyBorder(10, 12, 10, 12)
        ));
        component.setBackground(Color.WHITE);
        component.setForeground(TEXT_DARK);
    }

    private JTextArea createDisplayArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(Color.WHITE);
        area.setForeground(TEXT_DARK);
        area.setBorder(new EmptyBorder(12, 12, 12, 12));
        return area;
    }

    private void stylePrimaryButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(new EmptyBorder(11, 20, 11, 20));
    }

    private void styleOutlineButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(PRIMARY_DARK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(185, 201, 212)),
                new EmptyBorder(10, 18, 10, 18)
        ));
    }

    private void onLogin(ActionEvent actionEvent) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (authController.authenticate(username, password)) {
            loginStatus.setText(" ");
            setTitle("Student Management Desktop App");
            showCard("MAIN");
            refreshAllData();
        } else {
            loginStatus.setText("Login failed. Use admin/admin.");
        }
    }

    private void showCard(String cardName) {
        CardLayout layout = (CardLayout) rootPanel.getLayout();
        layout.show(rootPanel, cardName);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    private void refreshAllData() {
        refreshStudents();
        refreshCourses();
        refreshEnrollments();
        refreshResults();
        refreshAttendance();
    }

    private void refreshStudents() {
        List<Student> students = studentController.getAllStudents();
        StringBuilder sb = new StringBuilder();
        for (Student s : students) {
            sb.append("ID: ").append(s.getId())
                    .append("   Name: ").append(s.getName())
                    .append("   Email: ").append(s.getEmail())
                    .append("\n");
        }
        studentsArea.setText(sb.toString());
    }

    private void refreshCourses() {
        List<Course> courses = courseController.getAllCourses();
        StringBuilder sb = new StringBuilder();
        for (Course c : courses) {
            sb.append("ID: ").append(c.getId())
                    .append("   Name: ").append(c.getName())
                    .append("   Credits: ").append(c.getCredits())
                    .append("\n");
        }
        coursesArea.setText(sb.toString());
    }

    private void refreshEnrollments() {
        List<Enrollment> enrollments = enrollmentController.getAllEnrollments();
        StringBuilder sb = new StringBuilder();
        for (Enrollment e : enrollments) {
            sb.append("ID: ").append(e.getId())
                    .append("   Student ID: ").append(e.getStudentId())
                    .append("   Course ID: ").append(e.getCourseId())
                    .append("   Semester: ").append(e.getSemester())
                    .append("\n");
        }
        enrollmentsArea.setText(sb.toString());
    }

    private void refreshResults() {
        List<Result> results = resultController.getAllResults();
        StringBuilder sb = new StringBuilder();
        for (Result result : results) {
            sb.append("ID: ").append(result.getId())
                    .append("   Enrollment ID: ").append(result.getEnrollmentId())
                    .append("   Grade: ").append(result.getGrade())
                    .append("\n");
        }
        resultsArea.setText(sb.toString());
    }

    private void refreshAttendance() {
        List<Attendance> attendanceList = attendanceController.getAllAttendance();
        StringBuilder sb = new StringBuilder();
        for (Attendance attendance : attendanceList) {
            sb.append("ID: ").append(attendance.getId())
                    .append("   Enrollment ID: ").append(attendance.getEnrollmentId())
                    .append("   Date: ").append(attendance.getDate())
                    .append("   Status: ").append(attendance.getStatus())
                    .append("\n");
        }
        attendanceArea.setText(sb.toString());
    }

    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            GradientPaint gradient = new GradientPaint(0, 0, new Color(8, 82, 156), width, height, new Color(15, 166, 120));
            g2.setPaint(gradient);
            g2.fillRect(0, 0, width, height);

            g2.setColor(new Color(255, 255, 255, 28));
            g2.fillOval(width - 340, -60, 340, 340);
            g2.fillOval(-100, height - 260, 320, 320);
            g2.fillRoundRect(70, 70, 260, 170, 28, 28);

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(255, 255, 255, 40));
            g2.drawRoundRect(86, 92, 230, 126, 24, 24);
            g2.drawLine(width - 280, 120, width - 80, 220);
            g2.drawLine(width - 260, 220, width - 60, 320);
            g2.dispose();
        }
    }

    private static class StudentPhotoPanel extends JPanel {
        StudentPhotoPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(110, 110));
            setMaximumSize(new Dimension(110, 110));
            setMinimumSize(new Dimension(110, 110));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight()) - 8;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            g2.setColor(new Color(255, 255, 255, 220));
            g2.fillOval(x, y, size, size);
            g2.setColor(new Color(205, 216, 225));
            g2.setStroke(new BasicStroke(3f));
            g2.drawOval(x, y, size, size);

            g2.setColor(new Color(70, 97, 124));
            g2.fillOval(x + size / 3, y + size / 4, size / 3, size / 3);
            g2.fillRoundRect(x + size / 4, y + size / 2, size / 2, size / 4, 26, 26);

            g2.setColor(new Color(18, 155, 119));
            g2.setStroke(new BasicStroke(2f));
            g2.drawArc(x + 10, y + 10, size - 20, size - 20, 35, 280);
            g2.dispose();
        }
    }
}
