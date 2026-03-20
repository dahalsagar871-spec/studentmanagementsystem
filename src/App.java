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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import model.Attendance;
import model.Course;
import model.Enrollment;
import model.Result;
import model.Student;
import model.User;

public class App extends JFrame {
    private static final Color LOGIN_DARK = new Color(9, 49, 88);
    private static final Color LOGIN_LIGHT = new Color(25, 177, 127);
    private static final Color APP_BG = new Color(233, 239, 243);
    private static final Color SIDEBAR_BG = new Color(31, 47, 61);
    private static final Color SIDEBAR_ACTIVE = new Color(16, 170, 113);
    private static final Color SIDEBAR_TEXT = new Color(210, 222, 232);
    private static final Color TOPBAR_BG = new Color(242, 121, 53);
    private static final Color TOPBAR_BUTTON = new Color(208, 88, 22);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(35, 49, 61);
    private static final Color TEXT_MUTED = new Color(109, 125, 138);
    private static final Color BORDER = new Color(210, 220, 227);
    private static final Color BLUE_CARD = new Color(37, 188, 231);
    private static final Color GREEN_CARD = new Color(23, 186, 123);
    private static final Color ORANGE_CARD = new Color(243, 173, 40);
    private static final Color RED_CARD = new Color(232, 82, 82);
    private static final Color CALENDAR_GREEN = new Color(13, 177, 90);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MMMM yyyy");

    private final StudentController studentController;
    private final CourseController courseController;
    private final EnrollmentController enrollmentController;
    private final ResultController resultController;
    private final AttendanceController attendanceController;
    private final AuthController authController;

    private final JPanel rootPanel = new JPanel(new CardLayout());
    private final GradientPanel loginPanel = new GradientPanel();
    private final JPanel dashboardShell = new JPanel(new BorderLayout());
    private final JPanel contentCards = new JPanel(new CardLayout());
    private final Map<String, JButton> navButtons = new LinkedHashMap<>();

    private final JTextField usernameField = createTextField("admin");
    private final JPasswordField passwordField = createPasswordField("admin");
    private final JTextField teacherUsernameField = createTextField("");
    private final JPasswordField teacherPasswordField = createPasswordField("");
    private final JLabel loginStatus = new JLabel(" ");
    private final JLabel registerStatus = new JLabel(" ");

    private final JTextArea studentsArea = createDisplayArea();
    private final JTextArea coursesArea = createDisplayArea();
    private final JTextArea enrollmentsArea = createDisplayArea();
    private final JTextArea resultsArea = createDisplayArea();
    private final JTextArea attendanceArea = createDisplayArea();
    private final JTextArea recentActivityArea = createDisplayArea();

    private final JLabel sectionTitleLabel = new JLabel("Dashboard");
    private final JLabel sectionSubtitleLabel = new JLabel("Overview");
    private final JLabel userLabel = new JLabel("Admin");
    private final JLabel dateLabel = new JLabel();

    private final StatCard studentStatCard = new StatCard("Students", "0", "Registered learners", BLUE_CARD);
    private final StatCard courseStatCard = new StatCard("Courses", "0", "Available classes", GREEN_CARD);
    private final StatCard enrollmentStatCard = new StatCard("Enrollments", "0", "Active registrations", ORANGE_CARD);
    private final StatCard attendanceStatCard = new StatCard("Attendance", "0", "Attendance records", RED_CARD);

    private final JLabel calendarMonthLabel = new JLabel();
    private final CalendarPanel calendarPanel = new CalendarPanel();
    private final JLabel attendanceSummaryLabel = new JLabel("No attendance has been recorded yet.");
    private final JLabel resultSummaryLabel = new JLabel("No grades have been submitted yet.");
    private final JLabel overviewNoteLabel = new JLabel("Use the left menu to manage your system.");

    private User currentUser;

    public App() {
        super("Student Management System");
        this.studentController = new StudentController();
        this.courseController = new CourseController();
        this.enrollmentController = new EnrollmentController();
        this.resultController = new ResultController();
        this.attendanceController = new AttendanceController();
        this.authController = new AuthController();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 760));
        setSize(1280, 820);
        setLocationRelativeTo(null);

        initLoginPanel();
        initDashboardShell();

        rootPanel.add(loginPanel, "LOGIN");
        rootPanel.add(dashboardShell, "MAIN");
        setContentPane(rootPanel);
        showCard("LOGIN");
    }

    private void initLoginPanel() {
        loginPanel.setLayout(new GridBagLayout());

        JPanel loginCard = createWhiteCard();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setPreferredSize(new Dimension(440, 500));

        JLabel eyebrow = new JLabel("Desktop Edition");
        eyebrow.setFont(new Font("Segoe UI", Font.BOLD, 13));
        eyebrow.setForeground(LOGIN_LIGHT);
        eyebrow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel appTitle = new JLabel("Student Management");
        appTitle.setFont(TITLE_FONT);
        appTitle.setForeground(TEXT_DARK);
        appTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel loginHint = new JLabel("Sign in to open the new dashboard layout");
        loginHint.setFont(BODY_FONT);
        loginHint.setForeground(TEXT_MUTED);
        loginHint.setAlignmentX(Component.LEFT_ALIGNMENT);

        StudentPhotoPanel photoPanel = new StudentPhotoPanel();
        photoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton loginButton = new JButton("Sign In");
        stylePrimaryButton(loginButton);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.addActionListener(this::onLogin);

        JButton registerButton = new JButton("Register Teacher");
        styleOutlineButton(registerButton);
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.addActionListener(this::onRegisterTeacher);

        loginStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loginStatus.setForeground(new Color(196, 57, 52));
        loginStatus.setAlignmentX(Component.LEFT_ALIGNMENT);

        registerStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        registerStatus.setForeground(LOGIN_LIGHT.darker());
        registerStatus.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginCard.add(eyebrow);
        loginCard.add(Box.createRigidArea(new Dimension(0, 8)));
        loginCard.add(appTitle);
        loginCard.add(Box.createRigidArea(new Dimension(0, 6)));
        loginCard.add(loginHint);
        loginCard.add(Box.createRigidArea(new Dimension(0, 16)));
        loginCard.add(photoPanel);
        loginCard.add(Box.createRigidArea(new Dimension(0, 18)));
        loginCard.add(createFieldBlock("Username", usernameField));
        loginCard.add(Box.createRigidArea(new Dimension(0, 14)));
        loginCard.add(createFieldBlock("Password", passwordField));
        loginCard.add(Box.createRigidArea(new Dimension(0, 20)));
        loginCard.add(loginButton);
        loginCard.add(Box.createRigidArea(new Dimension(0, 12)));
        loginCard.add(loginStatus);
        loginCard.add(Box.createRigidArea(new Dimension(0, 22)));
        loginCard.add(createSectionLabel("Teacher Registration"));
        loginCard.add(Box.createRigidArea(new Dimension(0, 12)));
        loginCard.add(createFieldBlock("Teacher Username", teacherUsernameField));
        loginCard.add(Box.createRigidArea(new Dimension(0, 14)));
        loginCard.add(createFieldBlock("Teacher Password", teacherPasswordField));
        loginCard.add(Box.createRigidArea(new Dimension(0, 16)));
        loginCard.add(registerButton);
        loginCard.add(Box.createRigidArea(new Dimension(0, 10)));
        loginCard.add(registerStatus);

        loginPanel.add(loginCard, new GridBagConstraints());
    }

    private void initDashboardShell() {
        dashboardShell.setBackground(APP_BG);
        dashboardShell.add(createSidebar(), BorderLayout.WEST);
        dashboardShell.add(createWorkspacePanel(), BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setBorder(new EmptyBorder(18, 18, 18, 18));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel brand = new JLabel("StudentManagement");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 17));
        brand.setForeground(Color.WHITE);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleHint = new JLabel("Control Center");
        roleHint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleHint.setForeground(new Color(138, 159, 176));
        roleHint.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(brand);
        sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        sidebar.add(roleHint);
        sidebar.add(Box.createRigidArea(new Dimension(0, 24)));

        addNavButton(sidebar, "DASHBOARD", "Dashboard");
        addNavButton(sidebar, "STUDENTS", "Students");
        addNavButton(sidebar, "COURSES", "Courses");
        addNavButton(sidebar, "ENROLLMENTS", "Enrollments");
        addNavButton(sidebar, "RESULTS", "Results");
        addNavButton(sidebar, "ATTENDANCE", "Attendance");

        sidebar.add(Box.createVerticalGlue());

        JButton logoutButton = new JButton("Log Out");
        styleSidebarButton(logoutButton, false);
        logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutButton.addActionListener(e -> logout());
        sidebar.add(logoutButton);

        return sidebar;
    }

    private void addNavButton(JPanel sidebar, String pageKey, String label) {
        JButton button = new JButton(label);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        styleSidebarButton(button, false);
        button.addActionListener(e -> switchPage(pageKey, label));
        navButtons.put(pageKey, button);
        sidebar.add(button);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private JPanel createWorkspacePanel() {
        JPanel workspace = new JPanel(new BorderLayout());
        workspace.setOpaque(false);
        workspace.setBorder(new EmptyBorder(16, 16, 16, 16));
        workspace.add(createTopBar(), BorderLayout.NORTH);

        contentCards.setOpaque(false);
        contentCards.add(createDashboardPage(), "DASHBOARD");
        contentCards.add(createStudentPanel(), "STUDENTS");
        contentCards.add(createCoursePanel(), "COURSES");
        contentCards.add(createEnrollmentPanel(), "ENROLLMENTS");
        contentCards.add(createResultPanel(), "RESULTS");
        contentCards.add(createAttendancePanel(), "ATTENDANCE");
        workspace.add(contentCards, BorderLayout.CENTER);
        return workspace;
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout(12, 0));
        topBar.setBackground(TOPBAR_BG);
        topBar.setBorder(new EmptyBorder(10, 16, 10, 16));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        sectionTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitleLabel.setForeground(Color.WHITE);
        sectionSubtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sectionSubtitleLabel.setForeground(new Color(255, 233, 218));

        titlePanel.add(sectionTitleLabel);
        titlePanel.add(sectionSubtitleLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dateLabel.setForeground(Color.WHITE);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFocusable(false);
        refreshButton.setBackground(TOPBAR_BUTTON);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorder(new EmptyBorder(9, 16, 9, 16));
        refreshButton.addActionListener(e -> refreshAllData());

        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userLabel.setForeground(Color.WHITE);

        rightPanel.add(dateLabel);
        rightPanel.add(refreshButton);
        rightPanel.add(userLabel);

        topBar.add(titlePanel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);
        return topBar;
    }

    private JPanel createDashboardPage() {
        JPanel page = createDashboardPageContainer();
        page.add(createStatsStrip(), BorderLayout.NORTH);

        JPanel middle = new JPanel(new BorderLayout(16, 16));
        middle.setOpaque(false);
        middle.add(createCenterPanel(), BorderLayout.CENTER);
        middle.add(createRightRail(), BorderLayout.EAST);

        page.add(middle, BorderLayout.CENTER);
        return page;
    }

    private JPanel createStatsStrip() {
        JPanel strip = new JPanel(new GridLayout(1, 4, 16, 0));
        strip.setOpaque(false);
        strip.setBorder(new EmptyBorder(0, 0, 16, 0));
        strip.add(studentStatCard);
        strip.add(courseStatCard);
        strip.add(enrollmentStatCard);
        strip.add(attendanceStatCard);
        return strip;
    }

    private JPanel createCenterPanel() {
        JPanel center = createWhiteCard();
        center.setLayout(new BorderLayout(0, 14));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);

        JLabel heading = new JLabel("Calendar");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setForeground(TEXT_DARK);

        calendarMonthLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        calendarMonthLabel.setForeground(TEXT_MUTED);

        titleRow.add(heading, BorderLayout.WEST);
        titleRow.add(calendarMonthLabel, BorderLayout.EAST);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(6, 0, 0, 0));

        attendanceSummaryLabel.setFont(BODY_FONT);
        attendanceSummaryLabel.setForeground(Color.WHITE);
        resultSummaryLabel.setFont(BODY_FONT);
        resultSummaryLabel.setForeground(new Color(235, 255, 239));

        JPanel summaryPanel = new JPanel();
        summaryPanel.setBackground(CALENDAR_GREEN.darker());
        summaryPanel.setBorder(new EmptyBorder(12, 14, 12, 14));
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.add(attendanceSummaryLabel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        summaryPanel.add(resultSummaryLabel);

        footer.add(summaryPanel, BorderLayout.CENTER);

        center.add(titleRow, BorderLayout.NORTH);
        center.add(calendarPanel, BorderLayout.CENTER);
        center.add(footer, BorderLayout.SOUTH);
        return center;
    }

    private JPanel createRightRail() {
        JPanel rail = new JPanel();
        rail.setOpaque(false);
        rail.setPreferredSize(new Dimension(300, 0));
        rail.setLayout(new BoxLayout(rail, BoxLayout.Y_AXIS));

        JPanel quickActions = createWhiteCard();
        quickActions.setLayout(new BoxLayout(quickActions, BoxLayout.Y_AXIS));

        JLabel quickTitle = new JLabel("Quick Actions");
        quickTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        quickTitle.setForeground(TEXT_DARK);

        JButton addStudentButton = new JButton("Open Students");
        stylePrimaryButton(addStudentButton);
        addStudentButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addStudentButton.addActionListener(e -> switchPage("STUDENTS", "Students"));

        JButton addCourseButton = new JButton("Open Courses");
        styleOutlineButton(addCourseButton);
        addCourseButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addCourseButton.addActionListener(e -> switchPage("COURSES", "Courses"));

        overviewNoteLabel.setFont(BODY_FONT);
        overviewNoteLabel.setForeground(TEXT_MUTED);
        overviewNoteLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        quickActions.add(quickTitle);
        quickActions.add(Box.createRigidArea(new Dimension(0, 12)));
        quickActions.add(addStudentButton);
        quickActions.add(Box.createRigidArea(new Dimension(0, 10)));
        quickActions.add(addCourseButton);
        quickActions.add(Box.createRigidArea(new Dimension(0, 14)));
        quickActions.add(overviewNoteLabel);

        JPanel recentActivity = createWhiteCard();
        recentActivity.setLayout(new BorderLayout(0, 12));

        JLabel recentTitle = new JLabel("Recent Activity");
        recentTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        recentTitle.setForeground(TEXT_DARK);

        recentActivityArea.setRows(14);
        JScrollPane recentScroll = new JScrollPane(recentActivityArea);
        recentScroll.setBorder(BorderFactory.createLineBorder(BORDER));
        recentScroll.getVerticalScrollBar().setUnitIncrement(16);

        recentActivity.add(recentTitle, BorderLayout.NORTH);
        recentActivity.add(recentScroll, BorderLayout.CENTER);

        rail.add(quickActions);
        rail.add(Box.createRigidArea(new Dimension(0, 16)));
        rail.add(recentActivity);
        return rail;
    }

    private JPanel createStudentPanel() {
        JPanel panel = createManagementPage();
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
            refreshAllData();
            nameField.setText("");
            emailField.setText("");
        });

        panel.add(createFormCard("Student Management", "Create a student record", new JComponent[] {nameField, emailField},
                new String[] {"Name", "Email"}, addButton), BorderLayout.WEST);
        panel.add(createListCard("Student List", studentsArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCoursePanel() {
        JPanel panel = createManagementPage();
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
                refreshAllData();
                nameField.setText("");
                creditsField.setText("");
            } catch (NumberFormatException ex) {
                showError("Credits must be an integer.");
            }
        });

        panel.add(createFormCard("Course Management", "Set up a class offering", new JComponent[] {nameField, creditsField},
                new String[] {"Name", "Credits"}, addButton), BorderLayout.WEST);
        panel.add(createListCard("Course List", coursesArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createEnrollmentPanel() {
        JPanel panel = createManagementPage();
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
                refreshAllData();
                studentIdField.setText("");
                courseIdField.setText("");
                semesterField.setText("");
            } catch (NumberFormatException ex) {
                showError("Student ID and Course ID must be integers.");
            }
        });

        panel.add(createFormCard("Enrollment Management", "Register students into courses",
                new JComponent[] {studentIdField, courseIdField, semesterField},
                new String[] {"Student ID", "Course ID", "Semester"}, addButton), BorderLayout.WEST);
        panel.add(createListCard("Enrollment List", enrollmentsArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createResultPanel() {
        JPanel panel = createManagementPage();
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
                refreshAllData();
                enrollmentIdField.setText("");
                gradeField.setText("");
            } catch (NumberFormatException ex) {
                showError("Enrollment ID must be an integer.");
            }
        });

        panel.add(createFormCard("Result Management", "Record student grades", new JComponent[] {enrollmentIdField, gradeField},
                new String[] {"Enrollment ID", "Grade"}, addButton), BorderLayout.WEST);
        panel.add(createListCard("Result List", resultsArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAttendancePanel() {
        JPanel panel = createManagementPage();
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
                refreshAllData();
                enrollmentIdField.setText("");
                dateField.setText(LocalDate.now().toString());
                statusField.setText("");
            } catch (NumberFormatException ex) {
                showError("Enrollment ID must be an integer.");
            } catch (DateTimeParseException ex) {
                showError("Date must be in YYYY-MM-DD format.");
            }
        });

        panel.add(createFormCard("Attendance Management", "Track daily attendance",
                new JComponent[] {enrollmentIdField, dateField, statusField},
                new String[] {"Enrollment ID", "Date", "Status"}, addButton), BorderLayout.WEST);
        panel.add(createListCard("Attendance List", attendanceArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDashboardPageContainer() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 0, 0, 0));
        return panel;
    }

    private JPanel createManagementPage() {
        JPanel panel = createDashboardPageContainer();
        JPanel inner = new JPanel(new BorderLayout(16, 0));
        inner.setOpaque(false);
        panel.add(inner, BorderLayout.CENTER);
        return inner;
    }

    private JPanel createFormCard(String title, String subtitle, JComponent[] fields, String[] labels, JButton actionButton) {
        JPanel card = createWhiteCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(340, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_DARK);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(BODY_FONT);
        subtitleLabel.setForeground(TEXT_MUTED);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(subtitleLabel);
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
        JPanel card = createWhiteCard();
        card.setLayout(new BorderLayout(0, 14));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_DARK);

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    private JPanel createWhiteCard() {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(18, 18, 18, 18)
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

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        block.add(label);
        block.add(Box.createRigidArea(new Dimension(0, 6)));
        block.add(field);
        return block;
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(TEXT_DARK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
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
                BorderFactory.createLineBorder(BORDER),
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
        button.setFocusable(false);
        button.setBackground(SIDEBAR_ACTIVE);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(new EmptyBorder(11, 18, 11, 18));
    }

    private void styleOutlineButton(JButton button) {
        button.setFocusable(false);
        button.setBackground(Color.WHITE);
        button.setForeground(TEXT_DARK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(10, 18, 10, 18)
        ));
    }

    private void styleSidebarButton(JButton button, boolean active) {
        button.setFocusable(false);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setBorder(new EmptyBorder(11, 14, 11, 14));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(active ? SIDEBAR_ACTIVE : SIDEBAR_BG);
        button.setForeground(active ? Color.WHITE : SIDEBAR_TEXT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
    }

    private void onLogin(ActionEvent actionEvent) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        User loggedInUser = authController.authenticate(username, password);
        if (loggedInUser != null) {
            currentUser = loggedInUser;
            loginStatus.setText(" ");
            registerStatus.setText(" ");
            setTitle("Student Management Dashboard");
            configureDashboardForCurrentUser();
            showCard("MAIN");
            refreshAllData();
        } else {
            loginStatus.setText("Login failed. Use admin/admin or teacher/teacher.");
        }
    }

    private void onRegisterTeacher(ActionEvent actionEvent) {
        String username = teacherUsernameField.getText().trim();
        String password = new String(teacherPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            registerStatus.setForeground(new Color(196, 57, 52));
            registerStatus.setText("Enter a teacher username and password.");
            return;
        }

        if (authController.registerTeacher(username, password)) {
            registerStatus.setForeground(LOGIN_LIGHT.darker());
            registerStatus.setText("Teacher account created. You can sign in now.");
            teacherUsernameField.setText("");
            teacherPasswordField.setText("");
        } else {
            registerStatus.setForeground(new Color(196, 57, 52));
            registerStatus.setText("That username already exists or is invalid.");
        }
    }

    private void configureDashboardForCurrentUser() {
        String roleText = currentUser == null ? "Guest" : currentUser.getUsername() + " (" + currentUser.getRole() + ")";
        userLabel.setText(roleText);
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        overviewNoteLabel.setText(currentUser != null && "teacher".equalsIgnoreCase(currentUser.getRole())
                ? "Teacher view is ideal for monitoring activity and records."
                : "Admin view is ready for day-to-day management tasks.");
        switchPage("DASHBOARD", "Dashboard");
    }

    private void switchPage(String pageKey, String label) {
        CardLayout layout = (CardLayout) contentCards.getLayout();
        layout.show(contentCards, pageKey);
        sectionTitleLabel.setText(label);
        sectionSubtitleLabel.setText("Student Management System");
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            styleSidebarButton(entry.getValue(), entry.getKey().equals(pageKey));
        }
    }

    private void logout() {
        currentUser = null;
        usernameField.setText("admin");
        passwordField.setText("admin");
        showCard("LOGIN");
    }

    private void showCard(String cardName) {
        CardLayout layout = (CardLayout) rootPanel.getLayout();
        layout.show(rootPanel, cardName);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    private void refreshAllData() {
        List<Student> students = studentController.getAllStudents();
        List<Course> courses = courseController.getAllCourses();
        List<Enrollment> enrollments = enrollmentController.getAllEnrollments();
        List<Result> results = resultController.getAllResults();
        List<Attendance> attendanceList = attendanceController.getAllAttendance();

        refreshStudents(students);
        refreshCourses(courses);
        refreshEnrollments(enrollments);
        refreshResults(results);
        refreshAttendance(attendanceList);
        refreshDashboard(students, courses, enrollments, results, attendanceList);
    }

    private void refreshStudents(List<Student> students) {
        StringBuilder sb = new StringBuilder();
        for (Student student : students) {
            sb.append("ID: ").append(student.getId())
                    .append("   Name: ").append(student.getName())
                    .append("   Email: ").append(student.getEmail())
                    .append("\n");
        }
        studentsArea.setText(sb.length() == 0 ? "No students found." : sb.toString());
    }

    private void refreshCourses(List<Course> courses) {
        StringBuilder sb = new StringBuilder();
        for (Course course : courses) {
            sb.append("ID: ").append(course.getId())
                    .append("   Name: ").append(course.getName())
                    .append("   Credits: ").append(course.getCredits())
                    .append("\n");
        }
        coursesArea.setText(sb.length() == 0 ? "No courses found." : sb.toString());
    }

    private void refreshEnrollments(List<Enrollment> enrollments) {
        StringBuilder sb = new StringBuilder();
        for (Enrollment enrollment : enrollments) {
            sb.append("ID: ").append(enrollment.getId())
                    .append("   Student ID: ").append(enrollment.getStudentId())
                    .append("   Course ID: ").append(enrollment.getCourseId())
                    .append("   Semester: ").append(enrollment.getSemester())
                    .append("\n");
        }
        enrollmentsArea.setText(sb.length() == 0 ? "No enrollments found." : sb.toString());
    }

    private void refreshResults(List<Result> results) {
        StringBuilder sb = new StringBuilder();
        for (Result result : results) {
            sb.append("ID: ").append(result.getId())
                    .append("   Enrollment ID: ").append(result.getEnrollmentId())
                    .append("   Grade: ").append(result.getGrade())
                    .append("\n");
        }
        resultsArea.setText(sb.length() == 0 ? "No results found." : sb.toString());
    }

    private void refreshAttendance(List<Attendance> attendanceList) {
        StringBuilder sb = new StringBuilder();
        for (Attendance attendance : attendanceList) {
            sb.append("ID: ").append(attendance.getId())
                    .append("   Enrollment ID: ").append(attendance.getEnrollmentId())
                    .append("   Date: ").append(attendance.getDate())
                    .append("   Status: ").append(attendance.getStatus())
                    .append("\n");
        }
        attendanceArea.setText(sb.length() == 0 ? "No attendance records found." : sb.toString());
    }

    private void refreshDashboard(List<Student> students, List<Course> courses, List<Enrollment> enrollments,
            List<Result> results, List<Attendance> attendanceList) {
        studentStatCard.setValue(String.valueOf(students.size()), "Registered learners");
        courseStatCard.setValue(String.valueOf(courses.size()), "Available classes");
        enrollmentStatCard.setValue(String.valueOf(enrollments.size()), "Active registrations");
        attendanceStatCard.setValue(String.valueOf(attendanceList.size()), "Attendance records");

        YearMonth month = YearMonth.now();
        calendarMonthLabel.setText(month.format(MONTH_FORMAT));
        calendarPanel.updateMonth(month, attendanceList);

        long presentCount = attendanceList.stream().filter(a -> "present".equalsIgnoreCase(a.getStatus())).count();
        long absentCount = attendanceList.stream().filter(a -> "absent".equalsIgnoreCase(a.getStatus())).count();
        attendanceSummaryLabel.setText("Present: " + presentCount + "   Absent: " + absentCount + "   Total: " + attendanceList.size());
        resultSummaryLabel.setText(results.isEmpty()
                ? "No grades have been submitted yet."
                : "Results entered: " + results.size() + "   Latest grade: " + results.get(results.size() - 1).getGrade());

        recentActivityArea.setText(buildRecentActivityText(students, courses, enrollments, results, attendanceList));
    }

    private String buildRecentActivityText(List<Student> students, List<Course> courses, List<Enrollment> enrollments,
            List<Result> results, List<Attendance> attendanceList) {
        StringBuilder sb = new StringBuilder();
        sb.append("System snapshot").append("\n")
                .append("Students: ").append(students.size()).append("\n")
                .append("Courses: ").append(courses.size()).append("\n")
                .append("Enrollments: ").append(enrollments.size()).append("\n")
                .append("Results: ").append(results.size()).append("\n")
                .append("Attendance: ").append(attendanceList.size()).append("\n\n");

        if (!students.isEmpty()) {
            Student student = students.get(students.size() - 1);
            sb.append("Latest student").append("\n")
                    .append(student.getName()).append("  |  ").append(student.getEmail()).append("\n\n");
        }
        if (!courses.isEmpty()) {
            Course course = courses.get(courses.size() - 1);
            sb.append("Latest course").append("\n")
                    .append(course.getName()).append("  |  Credits: ").append(course.getCredits()).append("\n\n");
        }
        if (!enrollments.isEmpty()) {
            Enrollment enrollment = enrollments.get(enrollments.size() - 1);
            sb.append("Latest enrollment").append("\n")
                    .append("Student ").append(enrollment.getStudentId())
                    .append(" -> Course ").append(enrollment.getCourseId())
                    .append("  |  ").append(enrollment.getSemester()).append("\n\n");
        }
        if (!attendanceList.isEmpty()) {
            Attendance attendance = attendanceList.get(attendanceList.size() - 1);
            sb.append("Latest attendance").append("\n")
                    .append("Enrollment ").append(attendance.getEnrollmentId())
                    .append("  |  ").append(attendance.getDate())
                    .append("  |  ").append(attendance.getStatus()).append("\n");
        }

        return sb.toString();
    }

    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            GradientPaint gradient = new GradientPaint(0, 0, LOGIN_DARK, width, height, LOGIN_LIGHT);
            g2.setPaint(gradient);
            g2.fillRect(0, 0, width, height);

            g2.setColor(new Color(255, 255, 255, 28));
            g2.fillOval(width - 360, -80, 360, 360);
            g2.fillOval(-120, height - 280, 340, 340);
            g2.fillRoundRect(70, 70, 280, 180, 30, 30);

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(255, 255, 255, 40));
            g2.drawRoundRect(86, 92, 240, 130, 24, 24);
            g2.drawLine(width - 300, 120, width - 60, 220);
            g2.drawLine(width - 250, 220, width - 20, 320);
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

            g2.setColor(SIDEBAR_ACTIVE);
            g2.setStroke(new BasicStroke(2f));
            g2.drawArc(x + 10, y + 10, size - 20, size - 20, 35, 280);
            g2.dispose();
        }
    }

    private static class StatCard extends JPanel {
        private final JLabel titleLabel = new JLabel();
        private final JLabel valueLabel = new JLabel();
        private final JLabel detailLabel = new JLabel();

        StatCard(String title, String value, String detail, Color color) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(color);
            setBorder(new EmptyBorder(18, 18, 18, 18));

            titleLabel.setText(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            titleLabel.setForeground(new Color(255, 255, 255, 220));

            valueLabel.setText(value);
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
            valueLabel.setForeground(Color.WHITE);

            detailLabel.setText(detail);
            detailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            detailLabel.setForeground(new Color(255, 255, 255, 220));

            add(titleLabel);
            add(Box.createRigidArea(new Dimension(0, 18)));
            add(valueLabel);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(detailLabel);
        }

        void setValue(String value, String detail) {
            valueLabel.setText(value);
            detailLabel.setText(detail);
        }
    }

    private static class CalendarPanel extends JPanel {
        private YearMonth month = YearMonth.now();
        private final Map<Integer, Integer> attendanceByDay = new LinkedHashMap<>();

        CalendarPanel() {
            setPreferredSize(new Dimension(0, 350));
            setBackground(CALENDAR_GREEN);
            setBorder(new EmptyBorder(18, 18, 18, 18));
        }

        void updateMonth(YearMonth newMonth, List<Attendance> attendanceList) {
            month = newMonth;
            attendanceByDay.clear();
            for (Attendance attendance : attendanceList) {
                if (attendance.getDate() != null && YearMonth.from(attendance.getDate()).equals(month)) {
                    int day = attendance.getDate().getDayOfMonth();
                    attendanceByDay.put(day, attendanceByDay.getOrDefault(day, 0) + 1);
                }
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int headerHeight = 34;
            int columns = 7;
            int rows = 6;
            int cellWidth = Math.max(1, width / columns);
            int cellHeight = Math.max(1, (getHeight() - headerHeight) / rows);
            String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

            g2.setColor(new Color(255, 255, 255, 220));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            for (int i = 0; i < dayNames.length; i++) {
                int x = i * cellWidth + 10;
                g2.drawString(dayNames[i], x, 22);
            }

            int startOffset = convertToSundayIndex(month.atDay(1).getDayOfWeek());
            int day = 1;
            int totalDays = month.lengthOfMonth();

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    int index = row * columns + col;
                    int x = col * cellWidth;
                    int y = headerHeight + row * cellHeight;

                    g2.setColor(new Color(255, 255, 255, 45));
                    g2.drawRoundRect(x + 3, y + 3, cellWidth - 8, cellHeight - 8, 12, 12);

                    if (index >= startOffset && day <= totalDays) {
                        Integer count = attendanceByDay.get(day);
                        if (count != null) {
                            g2.setColor(new Color(255, 255, 255, 60));
                            g2.fillRoundRect(x + 5, y + 5, cellWidth - 12, cellHeight - 12, 12, 12);
                        }

                        g2.setColor(Color.WHITE);
                        g2.drawString(String.valueOf(day), x + 12, y + 22);

                        if (count != null) {
                            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                            g2.drawString(count + " rec", x + 12, y + 42);
                            g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                        }
                        day++;
                    }
                }
            }
            g2.dispose();
        }

        private int convertToSundayIndex(DayOfWeek dayOfWeek) {
            return dayOfWeek.getValue() % 7;
        }
    }
}
