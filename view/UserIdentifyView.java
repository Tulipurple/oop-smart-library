package view;

import controller.NavigationController;
import controller.UserController;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * 사용자 식별 및 로그인 (팀원의 BaseView 완벽 연동 버전)
 */
public class UserIdentifyView extends BaseView {

    private final UserController userController;
    private Consumer<User> onLoginSuccess;

    private JTextField studentIdField;
    private JTextField nameField;
    private JButton startButton;

    /**
     * 생성자 - 부모(BaseView)에게 navController를 전달합니다.
     */
    public UserIdentifyView(NavigationController navController, UserController userController) {
        super(navController);
        this.userController = userController;
    }

    @Override
    protected boolean isNavEnabled() { return false; }

    public void setOnLoginSuccess(Consumer<User> callback) {
        this.onLoginSuccess = callback;
    }

    /**
     *
     */
    @Override
    protected JPanel createContent() {
        // 단일 패널에 절대 좌표(null) 레이아웃 적용
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        // 1. 상단 시스템 타이틀 (팀원의 홈화면 디자인 규격 계승)
        JLabel titleLabel = new JLabel("도서관 예약 시스템");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(50, 60, 700, 50);
        panel.add(titleLabel);

        // 2. 입력 폼을 감싸는 패널
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(new Color(245, 245, 245));
        formPanel.setBounds(150, 160, 500, 180);

        Font labelFont = new Font("맑은 고딕", Font.BOLD, 16);

        // 학번 입력 영역
        JLabel idLabel = new JLabel("학번");
        idLabel.setFont(labelFont);
        idLabel.setBounds(60, 45, 80, 30);
        formPanel.add(idLabel);

        studentIdField = new JTextField();
        studentIdField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        studentIdField.setBounds(150, 45, 280, 30);
        formPanel.add(studentIdField);

        // 이름 입력 영역
        JLabel nameLabel = new JLabel("이름");
        nameLabel.setFont(labelFont);
        nameLabel.setBounds(60, 105, 80, 30);
        formPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        nameField.setBounds(150, 105, 280, 30);
        formPanel.add(nameField);

        panel.add(formPanel);

        // 3. 시스템 시작 버튼
        startButton = new JButton("시스템 시작하기");
        startButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        startButton.setBounds(300, 390, 200, 45);
        panel.add(startButton);

        // 이벤트 연결
        startButton.addActionListener(e -> handleLoginOrRegister());

        return panel;
    }

    /**
     * 로그인 버튼 클릭 시 처리 로직
     */
    private void handleLoginOrRegister() {
        String studentId = studentIdField.getText().trim();
        String name = nameField.getText().trim();

        if (studentId.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "학번과 이름을 모두 입력해 주세요.", 
                    "알림", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userController.loginOrRegister(studentId, name);

        if (user != null) {
            JOptionPane.showMessageDialog(this,
                    user.getName() + "님, 인증에 성공하였습니다.",
                    "로그인 성공",
                    JOptionPane.INFORMATION_MESSAGE);

            if (onLoginSuccess != null) {
                onLoginSuccess.accept(user);
            }

            navController.navigateTo(NavigationController.HOME);
            
        } else {
            JOptionPane.showMessageDialog(this, 
                    "입력 정보가 올바르지 않거나 등록되지 않은 정보입니다.", 
                    "로그인 실패", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}